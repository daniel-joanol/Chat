---
applyTo: "src/test/**/*.java"
---

All tests are **pure unit tests** — no `@SpringBootTest`, no application context.

## Framework and imports

```java
@ExtendWith(MockitoExtension.class)
class MyTest {
  @Mock  private MyDependency dep;
  @InjectMocks private MyClass sut;
}
```

Use `assertEquals`, `assertThrows`, `assertTrue` from `org.junit.jupiter.api.Assertions`.  
Use `verify` from `org.mockito.Mockito` to assert void method calls.  
Use `when(…).thenReturn(…)` with `any()` / `anyString()` / `any(Type.class)` as argument matchers.

## Random test data

Use EasyRandom with depth 2 for domain objects. Declare at field level, initialise in `@BeforeEach`:

```java
private EasyRandomParameters parameters = new EasyRandomParameters().randomizationDepth(2);
private EasyRandom generator = new EasyRandom(parameters);
private User user;

@BeforeEach
void setUp() {
  user = generator.nextObject(User.class);
}
```

## Mappers

**Never mock MapStruct mappers.** Inject the real implementation and set it via `ReflectionTestUtils`:

```java
private UserDtoMapper mapper = Mappers.getMapper(UserDtoMapper.class);

@BeforeEach
void setUp() {
  ReflectionTestUtils.setField(sut, "mapper", mapper);
}
```

## Test naming

```
test<MethodUnderTest>_<condition>_<expectedOutcome>
testAddContact_whenContactAlreadyExists_thenThrowConflictException
testGetById_whenOptionalIsEmpty_throwEntityNotFoundException
```

For the happy path (no special condition): `test<Method>_return<Result>`.

## Service tests (`application/service/`)

- Mock all domain ports (`ContactDao`, `SecurityUtil`, `UserService`, etc.).
- Generate domain objects with EasyRandom.
- Assert the returned object or verify the correct void method was called.
- Assert the correct typed exception is thrown (`BadRequestException`, `ConflictException`, `ForbiddenException`, etc.).

## Controller tests (`infrastructure/controller/`)

- Mock the domain service interface only.
- Use the real MapStruct DTO mapper injected via `ReflectionTestUtils`.
- Assert `response.getStatusCode()` against `HttpStatusCode.valueOf(NNN)`.
- Hardcode minimal request objects inline (Java records, e.g. `new ContactRequest("username")`).

## JPA DAO tests (`infrastructure/dao/jpa/`)

- Place test class in package `com.chat.server.infrastructure.dao.http.jpa` (existing convention).
- Mock the Spring Data JPA repository interface only.
- Use the real MapStruct entity mapper injected via `ReflectionTestUtils`.
- For `getById`/`getByUsername` style methods: test the `Optional.empty()` path and assert `EntityNotFoundException` is thrown with the id/username in `e.getInternalMessage()`.
- For simple delegation methods (e.g. `exists`): assert the return value matches the repository mock.
