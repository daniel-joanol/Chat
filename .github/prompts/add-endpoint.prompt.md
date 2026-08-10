---
description: "Add a new REST endpoint to an existing controller, or create a new controller, following project conventions."
agent: "agent"
tools: [read, edit, search, execute, todo]
argument-hint: "HTTP method, path, and what it does, e.g. 'GET /rest/v1/contact — list all contacts for the authenticated user'"
---

Add a new REST endpoint to the ChatServer project.

Reference [AGENTS.md](../AGENTS.md) — especially sections 6 (REST API), 7 (Security), and 11 (Exception Handling) — before writing any code.

## Steps

### 1. Service port method (`domain/service/`)
- Add the new use-case method to the relevant domain service interface.
- Signature uses domain model types only.

### 2. Application service implementation (`application/service/Default*Service.java`)
- Implement the new method.
- Apply business rules and authorization checks here (e.g. check resource ownership before operating on it).
- Use `SecurityUtil.getUsername()` to get the current user when needed.
- Throw typed exceptions: `EntityNotFoundException` (404), `ForbiddenException` (403), `ConflictException` (409), `BadRequestException` (400).

### 3. DAO port additions (if needed)
- Add any new query method to the domain DAO interface.
- Implement it in the JPA DAO adapter.
- Add a `@Query` to the Spring Data JPA repository if needed.

### 4. Request/response DTOs (`infrastructure/controller/request/`, `infrastructure/controller/response/`)
- Create Java records for request bodies with `@NotBlank` / `@Valid` / `@ValidPassword` where appropriate.
- Create response classes with Lombok `@Getter @Setter @Accessors(chain = true)`.

### 5. DTO mapper (`infrastructure/controller/mapper/`)
- Add mapping methods to the existing MapStruct mapper, or create a new one if a new entity type is involved.

### 6. Controller method (`infrastructure/controller/`)
- Add the endpoint to the existing controller, or create a new `@RestController` if it belongs to a new resource.
- New controllers must:
  - Use `@RequestMapping(Constants.<CONTROLLER_NAME>)` — add the constant to `Constants.java`.
  - Apply `@PreAuthorize(Constants.HAS_ROLE_USER)` or `HAS_ROLE_ADMIN` at class or method level as appropriate.
  - Include `@Tag`, `@Operation`, and `@ApiResponses` Swagger annotations.
  - Return `ResponseEntity<T>` with an explicit HTTP status.
- Public endpoints (no auth) go in `PublicController` under `@RequestMapping(Constants.PUBLIC_CONTROLLER)`.

### 7. Verify
- Run `./mvnw test`.
- Check Swagger UI at `http://localhost:20002/swagger-ui.html` if the server is running.
