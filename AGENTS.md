# AGENTS.md

## Project
Kotlin + Spring Boot 3 + PostgreSQL + Liquibase + JPA
Package: com.zor07.lastsave
Structure: layered (controllers, services, repositories, entities, dto)

## Conventions
- Enums as Kotlin enum class, separate file
- Column names in snake_case
- Liquibase migrations in XML format, path: src/main/resources/db/changelog/

## External APIs
- Always wrap external API calls in a dedicated RestTemplate bean
- Place the wrapper in its own bean class, not inline
- Response DTO classes go in a separate package, never as nested classes

## Architecture
- Controllers must contain minimal logic — delegate everything to services
- Services: always define an interface first, then provide an implementation
- No business logic in Repository layer
- No custom Repository methods unless explicitly asked
- Circular dependencies are not allowed. Fix by restructuring, not by @Lazy

## Testing
- Services: unit tests with mocks, use @ExtendWith(MockitoExtension::class)
- Custom repository methods: integration tests with @DataJpaTest
- Controllers: integration tests with @WebMvcTest, mock all services, test request/response parsing against hardcoded JSON strings

## VCS 
- Always do `git add` for every new file you create
- At the end always propose a commit message that would fit to changes you've made

## Build & Test
./gradlew build
./gradlew test