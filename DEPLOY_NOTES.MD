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

### Configure Keycloak
```http://localhost:8081/```
- Create client `chat` with client protocol `openid-connect`.
- Enable client authentication on settings.
- Copy the `client-secret` from credentials.
- Set the environmental property `KEYCLOAK_CLIENT_SECRET` with the client-secret value.
- Add roles `USER` and `ADMIN`.
- Add role ADMIN to the user admin (the one set on docker-compose.yml).

### Add user to database.
- Add the user to the table `_user` on database.