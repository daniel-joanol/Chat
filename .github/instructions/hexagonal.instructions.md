---
applyTo: "src/main/java/**/*.java"
---

This project uses **Hexagonal Architecture** (Ports and Adapters). Follow these rules for every Java file.

## Layer boundaries

| Package | Rule |
|---|---|
| `domain/` | No Spring, no JPA, no infrastructure imports. Only pure Java + Lombok. |
| `application/` | Spring `@Service` / `@Component` / `@Configuration` allowed. Inject domain interfaces only — never JPA repositories or HTTP clients directly. |
| `infrastructure/` | All framework code lives here. Implements domain ports. Never calls application services directly. |

## Naming conventions

- Domain service interface → `UserService`, `ContactService`
- Application service implementation → `DefaultUserService`, `DefaultContactService`
- Domain DAO interface → `UserDao`, `ContactDao`
- JPA DAO adapter → `JpaUserDao`, `JpaContactDao`
- JPA entity → `UserEntity`, `ContactEntity`
- MapStruct mapper → `UserEntityMapper`, `UserDtoMapper`

## Code comments and naming

- Prefer expressive method and variable names over explanatory comments. Method names should describe the action and intent (for example: `createUser`, `authenticate`, `markUserOnline`). Variable names should clearly indicate purpose and type; avoid abbreviations that obscure meaning.
- Only add comments when they add essential information that cannot be expressed via clear code: complex algorithm rationale, non-obvious domain rules, security considerations, or important cross-team agreements.
- Do not leave commented-out code in the repository. Remove dead code instead of commenting it out.
- Keep comments concise and focused on the "why" (motivation, constraints, trade-offs), not the "what" — the code (names, structure) should show what is happening.
- Commit policy: require human review before committing changes. Avoid automated or accidental commits that introduce commented-out code or unclear naming.

## Models

- Use `@Getter @Setter @Accessors(chain = true)` on domain models (fluent setters).
- Use Java `record` for inbound DTOs (controller request objects).
- No-arg constructor for domain models — Lombok handles it.

## Exceptions

- Always throw a typed subclass of `AbstractException`: `EntityNotFoundException`, `ForbiddenException`, `ConflictException`, `BadRequestException`, `InternalException`, `AuthenticationFailedException`.
- `externalMessage` = what the API client sees. `internalMessage` = what gets logged. Keep them separate.
- Never return error responses manually from controllers — the `GlobalDefaultExceptionHandler` handles all mapping to HTTP status codes.

## Security

- Use `SecurityUtil.getUsername()` (injected as a domain port) to get the authenticated user's username — never access `SecurityContextHolder` directly in domain or application layers.
- Enforce roles with `@PreAuthorize(Constants.HAS_ROLE_USER)` or `Constants.HAS_ROLE_ADMIN` at the controller level.

## Mapping

- Use MapStruct for all object-to-object mapping. Never write field-by-field mapping by hand.
- Mapper interfaces go in the `mapper/` sub-package of the layer that owns them.

## Database changes

- Every schema change requires a new Flyway migration file: `V1_0N__Description.sql` in `src/main/resources/db/migration/v1/`.
- Increment N from the last existing file. Migration files are immutable once committed.

## Constants

- Add new URL paths and role strings to `domain/constants/Constants.java` — never hardcode them in controllers.
