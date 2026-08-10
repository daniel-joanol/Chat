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

## Testing conventions

- In test sources (src/test/java), prefer using `var` for local variable declarations when the variable's type is obvious from the right-hand side. Examples: `var user = new User();`, `var response = sut.logout();`.
- Use explicit types instead of `var` when it improves readability, such as complex generic types or when the returned type is not apparent from the assignment.
- Test method names should describe behavior and expectation (e.g., `logout_setsUserOfflineAndSaves`). Variable names in tests should be descriptive and show intent (e.g., `savedUser`, `responseEntity`).
- Keep test code self-documenting: avoid comments that only restate what the code does. Use comments only for non-obvious test setup or important domain constraints.
- Keep tests fast and deterministic: prefer unit tests with mocks for logic verification and use integration tests only when necessary.

## Deprecated APIs

- Avoid using deprecated JDK, library, or framework APIs in new code. Deprecated APIs are a maintenance and compatibility risk.
- If using a deprecated API is temporarily unavoidable, add a short justification (TODO or comment) that references a tracker/ticket and a plan to replace it.
- Prefer stable, supported alternatives and update usages proactively when upgrading dependencies.
