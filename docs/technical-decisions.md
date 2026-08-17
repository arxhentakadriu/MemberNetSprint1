# Technical Decisions

This document records the main technical decisions made during MemberNet Sprint 1 and explains the reasoning behind them.

## Java 21

**Decision:** Java 21 was selected as the programming language.

**Reasoning:** Java was required by the project specification. Java 21 is a Long-Term Support release and provides a modern, stable platform for developing maintainable backend applications.

## Spring Boot

**Decision:** Spring Boot 3.5 was selected as the backend framework.

**Reasoning:** Spring Boot provides support for REST APIs, dependency injection, input validation, database integration, automated testing and operational monitoring. It also reduces the amount of configuration required to create a browser-based Java application.

## Maven

**Decision:** Maven was selected as the build and dependency-management tool.

**Reasoning:** Maven manages the project dependencies, compiles the Java source code, runs automated tests and packages the application as an executable JAR file. Its standard project structure also makes the application easier to understand and maintain.

The main build commands are:

```powershell
mvn clean test
mvn clean install
mvn spring-boot:run
```

## Layered Architecture

**Decision:** The application follows a layered software architecture.

The main layers are:

* Presentation layer: `AuthenticationController` and the browser interface.
* Application layer: `AuthenticationService`.
* Domain layer: `UserAccount` and `Role`.
* Persistence layer: repository interfaces, JPA entities and PostgreSQL adapters.

**Reasoning:** Separating responsibilities improves readability, maintainability, testing and future extensibility. For example, database implementation details can be changed without rewriting the authentication controller.

## REST API

**Decision:** Communication between the browser and backend uses a REST-style endpoint.

The login interface sends a `POST` request to:

```text
/api/auth/login
```

The credentials are sent as JSON, and the backend returns account information, permissions and the appropriate home-page information.

**Reasoning:** A REST boundary keeps the frontend and backend responsibilities separate and makes it possible to introduce another frontend or mobile client later.

## HTML, CSS and JavaScript

**Decision:** The browser interface was implemented with plain HTML, CSS and JavaScript.

**Reasoning:** Sprint 1 contains a small authentication workflow and does not require a frontend framework. Using browser-native technologies keeps the application simple and allows the frontend to be served directly by Spring Boot.

A framework such as React or Angular was not selected because it would introduce unnecessary complexity for the current requirements.

## PostgreSQL

**Decision:** PostgreSQL 18 was selected for persistent account storage.

**Reasoning:** PostgreSQL is a reliable relational database with strong Java and Spring support. It stores user accounts, member IDs, display names, password hashes and roles.

An in-memory repository was used during the initial prototype. It was later replaced with PostgreSQL so that accounts remain available after the application restarts.

## Spring Data JPA

**Decision:** Spring Data JPA and Hibernate were selected for database access.

**Reasoning:** JPA maps Java entities to relational database tables and reduces repetitive SQL code. Repository interfaces provide a clear separation between business logic and persistence logic.

The application still uses a persistence abstraction through `UserAccountRepository`, allowing the database implementation to be changed in the future.

## BCrypt Password Hashing

**Decision:** User passwords are stored as BCrypt hashes instead of plain text.

**Reasoning:** Plain-text passwords would create a serious security risk. BCrypt applies hashing and salting and is designed to make password-guessing attacks more expensive.

During authentication, the entered password is checked against the stored BCrypt hash. The original password cannot be recovered from the database.

## User Roles and Permissions

**Decision:** Sprint 1 supports the `MEMBER` and `ADMIN` roles.

**Reasoning:** These roles are sufficient for demonstrating permission loading and role-based home pages:

* A user with the `MEMBER` role receives the Member home page.
* A user with the `ADMIN` role receives the Administrator dashboard.

Role-based page selection demonstrates the project requirement, but production business endpoints should also enforce authorization on the server.

## Initial Database Users

**Decision:** A `DatabaseInitializer` creates demonstration users only when their usernames do not already exist.

**Reasoning:** This makes the project easier to run and demonstrate while preventing duplicate users whenever the application restarts.

Registration was not implemented because creating new accounts is outside the Sprint 1 requirements. Additional users can currently be inserted through database administration or added to the initializer.

## Error Handling

**Decision:** Authentication errors are handled centrally and returned as meaningful HTTP responses.

**Reasoning:** Invalid credentials should not cause an internal server error or expose sensitive implementation information. The browser receives a clear message that it can display to the user.

## Spring Boot Actuator

**Decision:** Spring Boot Actuator was added for operational monitoring.

**Reasoning:** Actuator provides a health endpoint that can be used to verify whether the application is running correctly:

```text
/actuator/health
```

The same monitoring capability can later support Docker and Kubernetes health checks, including readiness and liveness probes.

## Automated Testing

**Decision:** Spring Boot Test, JUnit and MockMvc were selected for automated backend testing.

**Reasoning:** Automated tests verify successful authentication and invalid-login handling. They help detect regressions when the implementation changes.

Database-specific integration testing can be expanded in later sprints using a dedicated test database or Testcontainers.

## Docker

**Decision:** The application and PostgreSQL database can be run with Docker Compose.

**Reasoning:** Docker provides a repeatable environment and reduces differences between developer machines. The Java application runs in a Java 21 container, while PostgreSQL runs in a separate database container.

The application container runs as a non-root user to improve container security.

## Environment Variables

**Decision:** Database connection values can be supplied through environment variables.

**Reasoning:** Passwords and environment-specific configuration should not be stored directly in source control. Docker Compose and future deployment environments can provide these values at runtime.

Example:

```properties
spring.datasource.password=${DB_PASSWORD}
```

A local `.env` file may supply the value, but `.env` must remain excluded from Git.

## Docker Volume

**Decision:** PostgreSQL data is stored in a named Docker volume.

**Reasoning:** Container files are normally removed with the container. A named volume preserves user accounts and other database information after containers are recreated.

## Git and GitHub

**Decision:** Git is used for version control, and the project is stored in a private GitHub repository.

**Reasoning:** Git records the development history and supports safe, incremental changes. A private repository avoids unintentionally exposing training material or configuration.

Generated build output, IDE files, environment files and credentials are excluded through `.gitignore`.

## Markdown Documentation

**Decision:** Project documentation is stored as Markdown files inside the Git repository.

**Reasoning:** Keeping documentation together with the source code allows it to evolve with the implementation. Documentation changes can also be reviewed and tracked through Git.

## AI-Assisted Development

**Decision:** AI was used as a development support tool.

**Reasoning:** AI assisted with requirement analysis, technology comparison, debugging, code review and documentation improvement. Generated suggestions were manually reviewed, tested and adjusted before being accepted.

