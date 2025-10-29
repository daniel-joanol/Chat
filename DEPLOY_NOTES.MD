# ChatServer Deployment Notes (Docker + Keycloak + Postgres)

## Prerequisites
- Docker and Docker Compose installed.
- Ports 8081 (Keycloak) and 5432 (Postgres) available. (Or change settings on docker-compose.yml)

## Installing Postgres and Keycloak
### Create docker container 
- Execute the docker compose on the background.
```docker compose up -d```

### Create Keycloak schema
- Add the schema keycloak inside the `chat-server` database.

### Generate an encryption key
- Generate an encryption key and them use it to encrypt the password that will be used for the default Keycloak user.
- Note: The default keycloak user is the one used in the keycloak petitions (to create user, update password, etc).
- Save the key as the environmental property CHAT_ENCRYPTION_KEY.
- Save the encrypted password as the value of the property `DEFAULT_KEYCLOAL_USER_PASS` on databse, table `property`.
- Code example: 
```
@SpringBootApplication
public class ChatServerApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ChatServerApplication.class, args);
    EncryptUtil encryptUtil = new AESEncryptUtil();
    String key = encryptUtil.generateKey();
    String encryptedPass = encryptUtil.encrypt("PASSWORD TO ENCRYPT", key);
    System.out.println("KEY: " + key);
    System.out.println("PASS: " + encryptedPass);
	}

}
```

### Configure Keycloak
```http://localhost:8081/```
- Create client `chat` with client protocol `openid-connect`.
- Enable client authentication on settings.
- Add roles `USER` and `ADMIN`.
- Copy the `client-secret` from credentials.
- Set the environmental property `KEYCLOAK_CLIENT_SECRET` with the client-secret value.
- Add client role ADMIN to the user `admin` (the one set on docker-compose.yml).
- Create the default user with username `default_internal_user`, with `no-actions-required` and role `ADMIN`.

### Add admin user to database.
- Add the user to the table `_user` on database.