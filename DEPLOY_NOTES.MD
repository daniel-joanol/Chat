# ChatServer Deployment Notes (Docker + Keycloak + Postgres)

This project is meant to be run with PostgreSQL and Keycloak in the same local stack. The setup below is a practical guide for bootstrapping the service and highlights the security patterns that are interesting to discuss in a job interview or portfolio review.

## Prerequisites
- Docker and Docker Compose installed.
- Ports 8081 (Keycloak) and 5432 (Postgres) available. If needed, update the mappings in `docker-compose.yml`.

## Installing Postgres and Keycloak
### Create docker container
- Start the local infrastructure in the background:
```bash
docker compose up -d
```

### Create a Keycloak schema
- Create the `keycloak` schema inside the `chat_server` database used by the application.

### Generate an encryption key
The service uses AES encryption to protect sensitive values before they are stored. In this project the default Keycloak password is encrypted before it is persisted to the `property` table.

- Generate a secure base64-encoded key.
- Store it as the environment variable `CHAT_ENCRYPTION_KEY`.
- Store the encrypted password as the value of `DEFAULT_KEYCLOAL_USER_PASS` in the database `property` table.

The code uses AES-256 in CBC mode with a random IV, then Base64-encodes the IV + ciphertext. This is a good example of a production-minded security pattern: the secret is never persisted in plaintext.

Code example:
```java
@SpringBootApplication
public class ChatServerApplication {

	  public static void main(String[] args) throws Exception {
	      SpringApplication.run(ChatServerApplication.class, args);

	      String generatedKey = new AESEncryptUtil("temporary-key").generateKey();
	      EncryptUtil encryptUtil = new AESEncryptUtil(generatedKey);
	      String encryptedPass = encryptUtil.encrypt("PASSWORD TO ENCRYPT", generatedKey);

	      System.out.println("KEY: " + generatedKey);
	      System.out.println("PASS: " + encryptedPass);
	  }
}
```

### Configure Keycloak
Visit `http://localhost:8081/`.

- Create client `chat` with protocol `openid-connect`.
- Enable client authentication in the client settings.
- Add roles `USER` and `ADMIN`.
- Copy the `client-secret` from the Credentials tab.
- Set the environment variable `KEYCLOAK_CLIENT_SECRET` with the client secret value.
- Add the client role `ADMIN` to the user `admin` configured in `docker-compose.yml`.
- Create the default internal user `default_internal_user` with `no-actions-required` and grant the `ADMIN` role from client `chat` and all required roles from the `master-realm` client.

### Add Keycloak IDs to the database
- Set the environment variable `KEYCLOAk_CLIENT_CONTAINER_ID` with the actual client UUID.
- Add the keycloak role IDs to the role table used by the application.

### Add admin user to the database
- Insert the admin user record into the `_user` table.

## Why this matters for the project
This setup is not just infrastructure wiring; it demonstrates a backend that integrates identity, secrets management, and secure messaging in a realistic way. For a portfolio or interview, these are strong talking points:

- RBAC with Keycloak and JWT validation.
- Cryptographic handling of sensitive configuration values.
- Database-backed user and role management with Flyway migrations.
- Real-time messaging with STOMP security boundaries.
