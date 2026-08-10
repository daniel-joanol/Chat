# ChatServer — Agent Documentation

Comprehensive reference for AI agents working on this codebase.

---

## 1. Project Overview

**ChatServer** is a real-time chat backend built with **Java 21 + Spring Boot 3.5.7**.  
It follows **Hexagonal Architecture** (Ports and Adapters).  
Identity management is fully delegated to **Keycloak** (OAuth2 / JWT).  
Persistence uses **PostgreSQL** with **Flyway** migrations.  
The project is a work in progress; WebSocket messaging is declared as a dependency but not yet implemented in business logic.

---

## 2. Tech Stack

| Concern | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.7 |
| Build | Maven (mvnw wrapper) |
| Security | Spring Security + OAuth2 Resource Server (JWT) |
| Identity Provider | Keycloak 26 |
| Database | PostgreSQL 16 |
| Migrations | Flyway 11.15.0 |
| ORM | Spring Data JPA / Hibernate |
| Mapping | MapStruct 1.6.3 |
| HTTP client | Unirest (kong) 4.5.1 |
| Validation | Jakarta Validation |
| API docs | SpringDoc OpenAPI (Swagger UI) |
| Encryption | AES/CBC/PKCS5Padding (custom util) |
| Lombok | Yes (all models and services) |
| Containerisation | Docker Compose (Postgres + Keycloak) |

---

## 3. Architecture — Hexagonal (Ports and Adapters)

```
┌──────────────────────────────────────────────┐
│               infrastructure/                │  ← Adapters
│  controller/   dao/jpa/   dao/http/          │
│  repository/jpa/  repository/http/           │
│  exception/                                  │
└────────────────────┬─────────────────────────┘
                     │ implements / injects
┌────────────────────▼─────────────────────────┐
│               application/                   │  ← Use-cases (orchestration)
│  service/Default*.java                       │
│  config/  util/                              │
└────────────────────┬─────────────────────────┘
                     │ implements / defines
┌────────────────────▼─────────────────────────┐
│                 domain/                      │  ← Pure business logic
│  model/   service/  dao/  enumerator/        │
│  constants/  util/                           │
└──────────────────────────────────────────────┘
```

### Layer responsibilities

| Layer | Package | Rule |
|---|---|---|
| **Domain** | `com.chat.server.domain` | No Spring, no infrastructure dependencies. Defines interfaces (ports) and models. |
| **Application** | `com.chat.server.application` | Spring services that implement domain service interfaces. Orchestrates domain DAOs and utilities. |
| **Infrastructure** | `com.chat.server.infrastructure` | Spring adapters: REST controllers, JPA repositories, HTTP clients, exception handlers. Implements domain DAO interfaces. |

---

## 4. Package Map

```
com.chat.server
├── ChatServerApplication.java          ← Spring Boot entry point
│
├── domain/
│   ├── model/
│   │   ├── User.java                   ← Core domain entity
│   │   ├── Contact.java                ← User friendship link
│   │   ├── Role.java                   ← Keycloak role
│   │   └── UserFactory.java            ← Factory: creates typed User instances
│   ├── service/                        ← Inbound ports (use-case interfaces)
│   │   ├── UserService.java
│   │   ├── ContactService.java
│   │   ├── RoleService.java
│   │   └── PropertyService.java
│   ├── dao/                            ← Outbound ports (storage/external interfaces)
│   │   ├── UserDao.java
│   │   ├── ContactDao.java
│   │   ├── RoleDao.java
│   │   ├── PropertyDao.java
│   │   └── AccessManagementDao.java    ← Keycloak port
│   ├── enumerator/
│   │   ├── UserRoleEnum.java           ← ADMIN | USER
│   │   ├── UserStatusEnum.java         ← OFFLINE | ONLINE | DO_NOT_DISTURB | AWAY
│   │   └── UserTypeEnum.java           ← INTERNAL | EXTERNAL
│   ├── constants/
│   │   └── Constants.java              ← URL prefixes, role strings, property keys
│   └── util/
│       ├── EncryptUtil.java            ← Encryption port
│       └── SecurityUtil.java           ← Current-user port
│
├── application/
│   ├── service/
│   │   ├── DefaultUserService.java     ← Implements UserService
│   │   ├── DefaultContactService.java  ← Implements ContactService
│   │   ├── DefaultRoleService.java     ← Implements RoleService
│   │   └── DefaultPropertyService.java ← Implements PropertyService
│   ├── config/
│   │   ├── SecurityConfig.java         ← Spring Security filter chain
│   │   └── OpenApiConfig.java          ← Swagger config
│   └── util/
│       ├── AESEncryptUtil.java         ← Implements EncryptUtil (AES/CBC/PKCS5)
│       ├── JWTSecurityUtil.java        ← Implements SecurityUtil (reads JWT claims)
│       └── TimeUtil.java               ← ZonedDateTime helper
│
└── infrastructure/
    ├── controller/
    │   ├── PublicController.java        ← POST /rest/v1/public/login, POST /rest/v1/public/logout, POST /rest/v1/public/user
    │   ├── ContactController.java       ← POST /rest/v1/contact, DELETE /rest/v1/contact/{id}
    │   ├── request/                     ← LoginRequest, UserRequest, ContactRequest (records)
    │   ├── response/                    ← UserResponse, ContactResponse
    │   ├── mapper/                      ← UserDtoMapper, ContactDtoMapper (MapStruct)
    │   └── annotation/
    │       ├── ValidPassword.java        ← Custom constraint annotation
    │       └── ValidPasswordImpl.java   ← Validates password complexity (12-30 chars, upper+lower+digit+special)
    ├── dao/
    │   ├── jpa/
    │   │   ├── JpaUserDao.java          ← Implements UserDao
    │   │   ├── JpaContactDao.java       ← Implements ContactDao
    │   │   ├── JpaRoleDao.java          ← Implements RoleDao
    │   │   └── JpaPropertyDao.java      ← Implements PropertyDao
    │   └── http/
    │       ├── AccessManagementHttpDao.java  ← Implements AccessManagementDao via Keycloak REST
    │       ├── mapper/AccessManagementHttpMapper.java  ← MapStruct mapper for Keycloak DTOs
    │       └── request/                 ← Keycloak-specific request POJOs
    ├── repository/
    │   ├── jpa/
    │   │   ├── UserJpaRepository.java   ← Spring Data JPA (entity: _user)
    │   │   ├── ContactJpaRepository.java
    │   │   ├── RoleJpaRepository.java
    │   │   ├── PropertyJpaRepository.java
    │   │   ├── entity/                  ← JPA entities (UserEntity, ContactEntity, RoleEntity, PropertyEntity)
    │   │   └── mapper/                  ← Entity ↔ Domain mappers (MapStruct)
    │   └── http/
    │       └── KeycloakHttpRepository.java  ← Raw Unirest HTTP calls to Keycloak Admin API
    └── exception/
        ├── AbstractException.java       ← Base: externalMessage (shown to client) + internalMessage (logged)
        ├── AuthenticationFailedException.java  ← → 401
        ├── BadRequestException.java            ← → 400
        ├── ConflictException.java              ← → 409
        ├── EntityNotFoundException.java        ← → 404
        ├── ForbiddenException.java             ← → 403
        ├── InternalException.java              ← → 500
        ├── controller/GlobalDefaultExceptionHandler.java  ← @ControllerAdvice
        └── response/
            ├── ErrorResponse.java       ← { traceId: UUID, message: String }
            └── ErrorResponseFactory.java
```

---

## 5. Domain Models

### User
```
UUID        id
UUID        keycloakId          ← Keycloak user UUID (populated after creation)
String      username            ← unique
String      firstName
String      lastName
String      email               ← unique
String      password            ← transient, not persisted (removed in V1_02)
ZonedDateTime createdAt
UserStatusEnum  status          ← OFFLINE | ONLINE | DO_NOT_DISTURB | AWAY
UserTypeEnum    type            ← INTERNAL | EXTERNAL
Role            role
Boolean     isEnabled
Boolean     isCreationCompleted ← false while Keycloak registration is in progress
List<Contact>   contacts
```

### Contact
```
UUID          id
User          user              ← owner
User          friend            ← target
ZonedDateTime createdAt
```
Static factory: `Contact.fromCreationRequest(user, friend)` — sets `createdAt = now()`.

### Role
```
Long          id
UserRoleEnum  name              ← ADMIN | USER
UUID          keycloakId        ← Keycloak role UUID
UUID          clientId          ← Keycloak client UUID (injected at runtime)
```

### UserFactory
Creates `User` instances with correct defaults:

| Method | Type | Role |
|---|---|---|
| `generateDefaultInternalUser(username, password)` | minimal stub | — |
| `generateInternalUser(user)` | INTERNAL | ADMIN |
| `generateExternalUser(user)` | EXTERNAL | USER |

Both `generateInternalUser` and `generateExternalUser` set:  
`status=OFFLINE`, `isEnabled=true`, `isCreationCompleted=false`, `createdAt=now()`.

---

## 6. REST API

Base path: `/rest/v1`  
Swagger UI: `http://localhost:20002/swagger-ui.html`  
OpenAPI JSON: `http://localhost:20002/v3/api-docs`

### Public endpoints (no auth required)

#### POST `/rest/v1/public/login`
Authenticate a user and receive a JWT.

**Request body:**
```json
{ "username": "string (required)", "password": "string (required)" }
```
**Responses:**
- `200` — plain-text JWT token (returned in response body, Content-Type: text/plain)
- `400` — missing field
- `401` — bad credentials (maps `EntityNotFoundException` → `AuthenticationFailedException`)

**Flow:** verifies user exists in DB → calls Keycloak OIDC token endpoint → sets user status to `ONLINE` → returns `access_token`.

---

#### POST `/rest/v1/public/logout`
Mark the current authenticated user as offline.

**Responses:**
- `204` — logout successful
- `5xx` — if the current authenticated user cannot be resolved from the security context

**Notes:**
- This endpoint obtains the current username from the JWT and updates the user's status to `OFFLINE`.
- It is exposed under the public controller but still requires a valid authenticated user in the security context.

---

#### POST `/rest/v1/public/user`
Register a new external user.

**Request body:**
```json
{
  "username": "string (required)",
  "firstName": "string (required)",
  "lastName": "string (required)",
  "email": "string (required)",
  "password": "string (required, 12-30 chars, must contain upper+lower+digit+special)"
}
```
**Responses:**
- `201` — `UserResponse` JSON
- `400` — missing/invalid field
- `409` — duplicate username or email

**Flow:** validates uniqueness → authenticates internal service user → creates user in Keycloak → saves to DB → fetches Keycloak UUID → sets role → sets password in Keycloak → marks `isCreationCompleted=true` → saves final state.

---

### Protected endpoints (JWT required)

All requests must include: `Authorization: Bearer <token>`

#### POST `/rest/v1/contact`
Add a contact to the authenticated user's list.  
**Required role:** `USER`

**Request body:**
```json
{ "contactUsername": "string (required)" }
```
**Responses:**
- `201` — `ContactResponse` JSON
- `400` — trying to add self
- `404` — username not found
- `409` — contact already exists

**Username resolution:** extracted from JWT claim `preferred_username` (fallback: `username`).

---

#### DELETE `/rest/v1/contact/{id}`
Remove a contact.  
**Required role:** `USER`

**Path param:** `id` (UUID)

**Responses:**
- `204` — deleted
- `403` — contact does not belong to the authenticated user

---

### Response shapes

**UserResponse**
```json
{
  "id": "uuid",
  "username": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "roleName": "ADMIN | USER",
  "contacts": [ContactResponse]
}
```

**ContactResponse**
```json
{
  "id": "uuid",
  "username": "string",
  "status": "OFFLINE | ONLINE | DO_NOT_DISTURB | AWAY"
}
```

**ErrorResponse** (all error codes)
```json
{
  "traceId": "uuid",
  "message": "string"
}
```

---

## 7. Security

- Spring Security with OAuth2 JWT Resource Server.
- JWT issued by Keycloak realm `master`.
- `SecurityConfig` permits: `/rest/v1/public/**`, `/v3/api-docs/**`, `/swagger-ui/**`.
- All other requests require a valid JWT.
- Method-level security enabled (`@EnableMethodSecurity`).
- `ContactController` uses `@PreAuthorize("hasRole('USER')")`.
- Current user is extracted by `JWTSecurityUtil` from `SecurityContextHolder` → JWT claim `preferred_username`.
- CSRF is disabled (stateless REST API).

---

## 8. Database Schema

PostgreSQL database: `chat_server` | schema: `chat`

### Table: `_user`
| Column | Type | Notes |
|---|---|---|
| id | UUID | PK, default `uuid_generate_v4()` |
| username | text | UNIQUE NOT NULL |
| email | text | UNIQUE NOT NULL |
| keycloak_id | UUID | Populated after Keycloak creation |
| first_name | text | NOT NULL |
| last_name | text | |
| created_at | TIMESTAMPTZ | default `now()` |
| status | text | UserStatusEnum |
| type | text | INTERNAL or EXTERNAL |
| role_id | int8 | FK → role(id) |
| is_enabled | bool | default true |
| is_creation_completed | bool | default true (flag for partial creation) |

### Table: `contact`
| Column | Type | Notes |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK → _user(id) |
| friend_id | UUID | FK → _user(id) |
| created_at | TIMESTAMPTZ | default `now()` |
| UNIQUE | (user_id, friend_id) | |

### Table: `role`
| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL | PK |
| name | text | UNIQUE NOT NULL — values: `ADMIN`, `USER` |
| keycloak_id | UUID | UNIQUE — Keycloak role UUID |

### Table: `property`
| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL | PK |
| name | text | UNIQUE NOT NULL |
| value | text | NOT NULL |

**Seeded properties:**
| name | purpose |
|---|---|
| `DEFAULT_KEYCLOAK_USER` | Username of the internal service account (`default_internal_user`) |
| `DEFAULT_KEYCLOAk_USER_PASS` | AES-encrypted password of the internal service account |

---

## 9. Keycloak Integration

`KeycloakHttpRepository` makes raw HTTP calls (Unirest) to the Keycloak Admin REST API.  
`AccessManagementHttpDao` wraps the repository and implements `AccessManagementDao`.

### Keycloak operations performed by the app

| Method | Keycloak endpoint | Usage |
|---|---|---|
| `login` | `POST /realms/{realm}/protocol/openid-connect/token` | Authenticate user or internal service account |
| `createUser` | `POST /admin/realms/{realm}/users` | Register new user |
| `getUser` | `GET /admin/realms/{realm}/users?search=&exact=true` | Fetch Keycloak UUID after creation |
| `updatePassword` | `PUT /admin/realms/{realm}/users/{id}/reset-password` | Set password |
| `addRoleToUser` | `POST /admin/realms/{realm}/users/{id}/role-mappings/clients/{clientId}` | Assign client role |
| `deleteUser` | `DELETE /admin/realms/{realm}/users/{id}` | Remove user |

### Internal service account pattern
All admin Keycloak operations are performed using a privileged service account (`default_internal_user`).  
The password is stored AES-encrypted in the `property` table.  
`DefaultPropertyService.getDefaultInternalUser()` decrypts it at runtime using `EncryptUtil`.

---

## 10. Encryption

**Class:** `AESEncryptUtil` implements `EncryptUtil`  
**Algorithm:** AES/CBC/PKCS5Padding, 256-bit key, random IV prepended to ciphertext.

| Method | Description |
|---|---|
| `generateKey()` | Generates a random 256-bit AES key, Base64-encoded |
| `encrypt(value)` | Encrypts using configured key (`encryption.key` property) |
| `encrypt(value, key)` | Encrypts using a specific key |
| `decrypt(value)` | Decrypts using configured key |
| `decrypt(value, key)` | Decrypts using a specific key |

Output format: Base64(`IV[16 bytes] + ciphertext`).

---

## 11. Exception Handling

All exceptions extend `AbstractException`, which carries:
- `externalMessage` — safe message returned to the API client
- `internalMessage` — full message logged internally

`GlobalDefaultExceptionHandler` (`@ControllerAdvice`) maps:

| Exception | HTTP Status |
|---|---|
| `MethodArgumentNotValidException` | 400 |
| `BadRequestException` | 400 |
| `AuthenticationFailedException` | 401 |
| `ForbiddenException` | 403 |
| `EntityNotFoundException` | 404 |
| `ConflictException` | 409 |
| `InternalException` | 500 |
| `JSONException`, `UnirestException`, etc. | 500 |

Every error response includes a `traceId` (UUID) that is also logged server-side.

---

## 12. Configuration & Environment Variables

### `application.properties` keys

| Property | Default/Pattern | Description |
|---|---|---|
| `server.port` | `20002` | HTTP port |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/chat_server` | DB URL |
| `spring.datasource.username` | `admin` | DB user |
| `spring.datasource.password` | `server_admin` | DB password |
| `spring.jpa.properties.hibernate.default_schema` | `chat` | DB schema |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `http://localhost:8081/realms/master` | JWT issuer |
| `spring.security.oauth2.client.registration.keycloak.client-id` | `chat` | OAuth client ID |
| `spring.security.oauth2.client.registration.keycloak.client-secret` | `${KEYCLOAK_CLIENT_SECRET}` | ← env var |
| `keycloak.url` | `http://localhost:8081` | Keycloak base URL |
| `keycloak.realm` | `master` | Realm name |
| `keycloak.client.container.id` | `${KEYCLOAk_CLIENT_CONTAINER_ID}` | ← env var (UUID of the `chat` client) |
| `encryption.key` | `${CHAT_ENCRYPTION_KEY:PLACEHOLDER_...}` | ← env var |

### Required environment variables

| Variable | Description |
|---|---|
| `KEYCLOAK_CLIENT_SECRET` | Client secret from Keycloak `chat` client credentials tab |
| `KEYCLOAk_CLIENT_CONTAINER_ID` | UUID of the Keycloak `chat` client (used for role assignment) |
| `CHAT_ENCRYPTION_KEY` | Base64-encoded AES-256 key used to encrypt/decrypt the internal user password |

---

## 13. Infrastructure (Docker)

`docker/docker-compose.yml` starts two services:

| Service | Image | Port | Notes |
|---|---|---|---|
| `postgres` | `postgres:16` | 5432 | DB: `chat_server`, user: `admin`, pass: `server_admin` |
| `keycloak` | `quay.io/keycloak/keycloak:26.0.0` | 8081 (→ 8080) | Dev mode, uses same Postgres DB, schema `keycloak` |

Start: `docker compose up -d` (from `docker/` directory).

---

## 14. Flyway Migrations

Location: `src/main/resources/db/migration/v1/`  
Schema: `chat`

| File | Description |
|---|---|
| `V1_01__Create_schema.sql` | Creates `_user` and `contact` tables, enables `uuid-ossp` extension |
| `V1_02__Add_role_schema.sql` | Creates `role` and `property` tables, seeds roles and properties, refactors `_user` (adds first_name, last_name, role_id FK, is_enabled, is_creation_completed; removes password column) |
| `V1_03__Update_contact_schema.sql` | Removes deprecated `is_active` column from `contact` |

---

## 15. Build & Run

```bash
# Run all tests
./mvnw test

# Build (skip tests)
./mvnw package -DskipTests

# Run locally
./mvnw spring-boot:run
```

Server starts at `http://localhost:20002`.

---

## 16. Key Conventions & Patterns

1. **Lombok everywhere** — `@Getter`, `@Setter`, `@Accessors(chain=true)`, `@RequiredArgsConstructor`. Domain models use fluent (chain) setters.
2. **Records for inbound DTOs** — `LoginRequest`, `UserRequest`, `ContactRequest` are Java records with validation annotations.
3. **MapStruct for all mappings** — Domain ↔ Entity, Domain ↔ DTO, Domain ↔ Keycloak request.
4. **Internal vs External users** — `INTERNAL` users are service accounts; `EXTERNAL` are real end-users created via the public API.
5. **`isCreationCompleted` flag** — Set to `false` at the start of the user creation flow and `true` only after all Keycloak steps succeed.
6. **No password stored in DB** — `_user` table does not have a password column (removed in V1_02). Passwords live in Keycloak only.
7. **Dual message on exceptions** — `externalMessage` is API-safe; `internalMessage` is for logs. `GlobalDefaultExceptionHandler` logs the internal message with a `traceId`.
8. **Service account for admin ops** — Any Keycloak admin operation (create, delete, assign role) is performed by first authenticating the `default_internal_user` service account.
9. **URL prefix** — All REST paths start with `/rest/v1`.
10. **Role enforcement** — `SecurityConfig` handles global auth; method-level `@PreAuthorize` enforces role-specific access.

---

## 17. Known Limitations / Future Work

- WebSocket dependency is included but real-time messaging is not yet implemented.
- Only two controllers exist (`PublicController`, `ContactController`). User management (get user, update, delete) is implemented in the service layer but not exposed via a controller yet.
- Keycloak realm is hardcoded to `master`; production deployments should use a dedicated realm.
- CSRF is disabled — acceptable for stateless JWT APIs, but worth reviewing if session-based flows are added.
