---
description: "Add a full domain feature (model, port, service, JPA adapter) following hexagonal architecture conventions."
agent: "agent"
tools: [read, edit, search, execute, todo]
argument-hint: "Feature name and description, e.g. 'Message entity that belongs to a Contact'"
---

Add a new domain feature to the ChatServer project following its hexagonal architecture.

Reference [AGENTS.md](../AGENTS.md) for conventions, package structure, and naming patterns before writing any code.

## Steps

### 1. Domain model (`domain/model/`)
- Create a new class (e.g. `Message.java`) with Lombok `@Getter @Setter @Accessors(chain = true)`.
- Fields use domain types (`UUID`, `ZonedDateTime`, other domain models). No Spring/JPA annotations.
- Add a static factory method if creation has defaults (see `Contact.fromCreationRequest` as a pattern).

### 2. DAO port (`domain/dao/`)
- Create an interface (e.g. `MessageDao.java`) with the minimum CRUD operations needed.
- Method signatures use only domain types — no JPA entities.

### 3. Service port (`domain/service/`)
- Create an interface (e.g. `MessageService.java`) with use-case methods.

### 4. Application service (`application/service/`)
- Create `Default<Feature>Service.java` annotated with `@Service @RequiredArgsConstructor`.
- Inject domain ports (DAO and service interfaces) only — no repositories directly.
- Implement all business rules and validations here.
- Use `SecurityUtil.getUsername()` to resolve the current user when needed.

### 5. JPA entity (`infrastructure/repository/jpa/entity/`)
- Create `<Feature>Entity.java` with `@Entity @Table(name = "...")` and JPA annotations.
- Primary key: `UUID` with `@GeneratedValue`.

### 6. JPA repository (`infrastructure/repository/jpa/`)
- Create `<Feature>JpaRepository.java` extending `JpaRepository<Entity, UUID>`.
- Add `@Query` methods only when Spring Data method names are insufficient.

### 7. Entity mapper (`infrastructure/repository/jpa/mapper/`)
- Create a MapStruct interface `<Feature>EntityMapper.java` annotated with `@Mapper(componentModel = "spring")`.
- Methods: `toDomain(entity)` and `toEntity(domain)`.

### 8. JPA DAO adapter (`infrastructure/dao/jpa/`)
- Create `Jpa<Feature>Dao.java` annotated with `@Component @RequiredArgsConstructor`.
- Implement the domain DAO interface created in step 2.
- Throw typed exceptions: `EntityNotFoundException` for missing records, `InternalException` for unexpected failures.

### 9. Flyway migration
- Create `V1_0N__Add_<feature>_schema.sql` in `src/main/resources/db/migration/v1/` (increment version number).
- Add the table definition matching the JPA entity.

### 10. Verify
- Run `./mvnw test` — fix any failures before finishing.
- Confirm no `domain/` class imports anything from `org.springframework` or `jakarta.persistence`.
