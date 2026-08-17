# Assumptions and Risks

This document records the main assumptions, limitations and identified risks for MemberNet Sprint 1.

## 1. Assumptions

### 1.1 Existing Accounts

User accounts must exist in PostgreSQL before a user can log in.

Self-service registration is not included in Sprint 1. Demonstration accounts are created through `DatabaseInitializer` when they do not already exist.

### 1.2 Supported Roles

The current application supports two roles:

```text
MEMBER
ADMIN
```

These roles are sufficient for the Sprint 1 authentication workflow.

Additional roles and permissions may be introduced in future sprints.

### 1.3 Database Availability

PostgreSQL is expected to be available before MemberNet starts.

For local development, PostgreSQL is expected to run on the configured host and port.

For Docker execution, MemberNet connects to the PostgreSQL service through:

```text
jdbc:postgresql://postgres:5432/membernet
```

### 1.4 Environment Configuration

The database password is expected to be provided through the `DB_PASSWORD` environment variable.

The password must not be stored directly in the source code or committed to GitHub.

### 1.5 Browser Environment

The application is expected to run in a modern browser with JavaScript enabled.

The user interface is designed for normal desktop and mobile browser operation.

### 1.6 Training Scope

MemberNet Sprint 1 is a training application.

It demonstrates authentication, account loading, permissions, role-based pages, PostgreSQL integration, automated testing and Docker deployment.

It is not intended to be a production-ready identity management system.

### 1.7 Kubernetes Scope

Kubernetes deployment belongs to Sprint 2.

Sprint 1 only prepares the application for future Kubernetes use through containerization, environment-based configuration and a health endpoint.

### 1.8 Monitoring Scope

Spring Boot Actuator provides basic health monitoring through:

```text
/actuator/health
```

Advanced metrics, alerts, dashboards and centralized logging are outside the Sprint 1 scope.

## 2. Identified Risks

### 2.1 Exposed Database Credentials

**Risk:** A database password could accidentally be committed to GitHub through `application.properties`, `application.yml` or `docker-compose.yml`.

**Impact:** Unauthorized users could gain access to the database if the credentials were valid in an accessible environment.

**Mitigation:**

* Use the `DB_PASSWORD` environment variable.
* Review `git status` and staged changes before every commit.
* Do not place real credentials in documentation or screenshots.
* Replace any credential immediately if it is accidentally exposed.

### 2.2 Weak Demonstration Passwords

**Risk:** Demonstration accounts use simple passwords for training and testing.

**Impact:** These accounts would be insecure in a production environment.

**Mitigation:**

* Use demonstration credentials only in local training environments.
* Do not use them in production.
* Introduce stronger password requirements in future versions.
* Configure initial credentials through secure environment variables or an administrator setup process.

### 2.3 Browser Session Storage

**Risk:** Sprint 1 uses browser-side state to retain account information after login.

**Impact:** Client-side state is not sufficient for secure production authentication and can be modified by the browser user.

**Mitigation:**

* Treat browser state as a Sprint 1 user-interface mechanism only.
* Add Spring Security in a future sprint.
* Use secure server-side sessions or secure token cookies.
* Enforce permissions on backend endpoints, not only in the user interface.

### 2.4 Missing Production Authorization

**Risk:** The current role result is mainly used to select the Member or Administrator user interface.

**Impact:** Hiding a page in the browser does not protect future business endpoints.

**Mitigation:**

* Add Spring Security authorization.
* Protect backend endpoints according to the authenticated user's roles.
* Add automated authorization tests.

### 2.5 Database Availability

**Risk:** Authentication cannot complete if PostgreSQL is unavailable.

**Impact:** Users cannot log in or load their account information.

**Mitigation:**

* Use the Docker Compose PostgreSQL health check.
* Monitor the application through Spring Boot Actuator.
* Add database backup, recovery and operational monitoring for production.
* Provide clear startup and database connection logs.

### 2.6 Database Data Loss

**Risk:** Docker database data can be deleted if the volume is intentionally removed.

**Impact:** Stored accounts and roles may be lost.

**Mitigation:**

* Use a persistent Docker volume.
* Use `docker compose down` for normal shutdown.
* Avoid `docker compose down -v` unless deletion is intentional.
* Introduce database backups for production environments.

### 2.7 Automatic Schema Updates

**Risk:** The training configuration can allow Hibernate to update the database schema automatically.

**Impact:** Automatic schema changes may be unpredictable in a production environment.

**Mitigation:**

* Use automatic updates only during development.
* Introduce Flyway or Liquibase migrations in a future version.
* Review and version every database schema change.

### 2.8 Docker Configuration Errors

**Risk:** Incorrect service names, environment variables, ports or volume paths can prevent the containers from starting.

**Impact:** MemberNet may be unable to connect to PostgreSQL.

**Mitigation:**

* Validate the configuration with `docker compose config`.
* Use `postgres` as the database hostname inside Docker.
* Check container status with `docker compose ps`.
* Review logs with `docker compose logs`.
* Keep PostgreSQL 18 data mounted at the supported volume location.

### 2.9 Port Conflicts

**Risk:** Port `8080` may already be used by another local application or Docker container.

**Impact:** MemberNet cannot start or cannot expose its browser interface.

**Mitigation:**

* Stop the existing process or container.
* Check running containers with `docker ps`.
* Change the published host port when necessary.

### 2.10 Demonstration Account Initialization

**Risk:** `DatabaseInitializer` only creates an account if its username does not already exist.

**Impact:** Changing account information in the initializer does not automatically update an existing database row.

**Mitigation:**

* Update existing account information through PostgreSQL.
* Introduce database migrations or an administrator account-management interface.
* Keep initializer data limited to training use.

### 2.11 Limited Automated Test Coverage

**Risk:** Sprint 1 currently contains a small number of automated controller tests.

**Impact:** Some service, repository, Docker or database problems may not be detected automatically.

**Mitigation:**

* Retain manual test cases.
* Add service unit tests.
* Add repository integration tests.
* Use Testcontainers for isolated PostgreSQL tests.
* Add a CI workflow that runs tests for each each Git push.

### 2.12 Health Endpoint Limitations

**Risk:** A basic `UP` response does not provide complete monitoring or performance information.

**Impact:** Some operational problems may not be detected by the current health check.

**Mitigation:**

* Add database health details where appropriate.
* Add application metrics.
* Introduce centralized logs, alerts and dashboards in future sprints.
* Use readiness and liveness probes when Kubernetes is implemented.

### 2.13 AI-Generated Errors

**Risk:** AI-generated code or documentation may contain incorrect assumptions, outdated suggestions, syntax errors or security problems.

**Impact:** Unverified suggestions may introduce defects or inaccurate documentation.

**Mitigation:**

* Review every important AI suggestion.
* Compile and test generated code.
* Compare documentation with the actual implementation.
* Use official documentation when verifying technical decisions.
* Keep human responsibility for final decisions.

### 2.14 Documentation Becoming Outdated

**Risk:** Documentation may continue to describe an earlier implementation, such as the removed in-memory repository.

**Impact:** Reviewers may misunderstand the final architecture.

**Mitigation:**

* Update documentation whenever the implementation changes.
* Remove references to components that no longer exist.
* Review README and all files in `docs` before project delivery.
* Commit documentation changes together with related code changes.

## 3. Risk Review Summary

The main Sprint 1 risks are acceptable for a training application.

The highest-priority controls are:

1. Keep database credentials outside source control.
2. Store passwords using BCrypt.
3. Verify role behavior through testing.
4. Preserve PostgreSQL data through Docker volumes.
5. Review AI-generated work manually.
6. Keep the documentation consistent with the final implementation.
7. Introduce production authentication and authorization controls in future sprints.
