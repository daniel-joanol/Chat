---
applyTo: "src/test/java/**/*.java"
---

## Testing conventions

- In test sources (src/test/java), prefer using `var` for local variable declarations when the variable's type is obvious from the right-hand side. Examples: `var user = new User();`, `var response = sut.logout();`.
- Use explicit types instead of `var` when it improves readability, such as complex generic types or when the returned type is not apparent from the assignment.
- Test method names should describe behavior and expectation (e.g., `logout_setsUserOfflineAndSaves`). Variable names in tests should be descriptive and show intent (e.g., `savedUser`, `responseEntity`).
- Keep test code self-documenting: avoid comments that only restate what the code does. Use comments only for non-obvious test setup or important domain constraints.
- Keep tests fast and deterministic: prefer unit tests with mocks for logic verification and use integration tests only when necessary.

## Mappers in tests

- Do not mock mappers in tests. Mappers are simple, generated code and mocking them hides mapping issues. In unit tests prefer to use the real mapper implementation (instantiate with `org.mapstruct.factory.Mappers.getMapper(YourMapper.class)` or include the mapper bean in a lightweight Spring test context).
- Only mock mappers in exceptional cases with a clear justification documented in the test.
