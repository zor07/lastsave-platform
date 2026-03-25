# lastsave — платформа Java-курса

## Project
Kotlin + Spring Boot 3 + PostgreSQL + Liquibase + Exposed
Package: com.zor07.lastsave
Structure: layered (controllers, services, repositories, model, table, dto)

## Current state
Project is being refactored after AI-generated code. Many things may not work.
Current milestone: fix the project, implement AI PR review, run full learning cycle through the bot.

## DO NOT write or modify tests. Tests will be updated later.

## Бизнес-логика
- Регистрация: /start в боте → GitHub OAuth → создание студента
- Прогресс: шедулер каждую минуту → getOrStartProgress → отправка следующего сообщения
- Ожидание: NOTHING | CALLBACK | PR — определяет когда двигаться дальше
- Старт блока: создаёт репо из шаблона, добавляет студента коллаборатором, шлёт ссылку

## Key entities
Student, StudentProgress, Block, Topic, Section, Material, Message, MessageLog

## Conventions
- Enums as Kotlin enum class, separate file
- Column names in snake_case
- Liquibase migrations in XML format, path: src/main/resources/db/changelog/
- Prefer SQL over JPA methods with long names
- Domain model classes (read/write DTOs) go in package `model`
- Exposed Table objects go in package `table`

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

## VCS
- Always do `git add` for every new file you create
- At the end always propose a commit message that would fit to changes you've made

## Build & Test
./gradlew build
./gradlew test
