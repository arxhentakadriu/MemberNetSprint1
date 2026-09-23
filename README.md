# MemberNet Sprint 2

[![Backend Tests](https://github.com/arxhentakadriu/MemberNetSprint1/actions/workflows/backend-tests.yml/badge.svg?branch=sprint2)](https://github.com/arxhentakadriu/MemberNetSprint1/actions/workflows/backend-tests.yml)

MemberNet is a browser-based membership management application developed as part of the MemberNet AI Developer Training Project.

Sprint 2 extends the Sprint 1 authentication workflow with associations, memberships, contextual roles and permissions, guardianship relationships, payment obligations, PostgreSQL persistence, API documentation, automated testing, Docker Compose and Kubernetes deployment.

## Features

- Login with email and password
- BCrypt password hashing
- Session-based authentication
- Global logout and session invalidation
- Protected REST API endpoints
- User account persistence
- Association management
- Membership management
- Contextual roles and permissions
- Membership-role assignment
- Role-permission assignment
- Backend authorization enforcement
- Guardianship relationships
- Payment obligations and payment history
- Request validation and consistent API errors
- PostgreSQL persistence
- Flyway database migrations
- Swagger and OpenAPI documentation
- Spring Boot Actuator health probes
- Functional browser interface
- Docker and Docker Compose support
- Kubernetes deployment configuration
- Automated backend integration tests
- GitHub Actions continuous integration

## Technologies

- Java 21
- Spring Boot 3.5
- Maven
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- PostgreSQL 18
- Flyway
- BCrypt
- Spring Boot Actuator
- Springdoc OpenAPI and Swagger UI
- HTML, CSS and JavaScript
- Docker and Docker Compose
- Kubernetes
- JUnit 5
- MockMvc
- Git and GitHub Actions

## Architecture

MemberNet follows a layered architecture:

```text
Browser / Swagger UI
        |
        v
REST Controllers
        |
        v
Application Services
        |
        v
Repositories
        |
        v
PostgreSQL
```

Main layers:

- **Presentation layer:** HTML, CSS and JavaScript
- **Controller layer:** REST endpoints, authentication and request validation
- **Service layer:** business rules, authorization and workflow processing
- **Repository layer:** Spring Data JPA database access
- **Database layer:** PostgreSQL schema managed by Flyway
- **Infrastructure layer:** Docker, Kubernetes, Actuator and GitHub Actions

## Core Business Model

Sprint 2 separates application identity from organizational participation.

```text
User Account
     |
     v
Membership
     |
     v
Association
     |
     v
Role
     |
     v
Permission
```

A user account may have memberships in multiple associations. Roles are assigned through memberships, which ensures that permissions remain within the correct association context.

## Main Modules

### Authentication

Provides:

- Login through `POST /api/auth/login`
- Current session through `GET /api/auth/session`
- Logout through `POST /api/auth/logout`
- Session-based protection for `/api/**`
- BCrypt password verification
- HTTP `401` responses when authentication is missing

### Associations

Provides creation and retrieval of associations.

Base endpoint:

```text
/api/associations
```

### Memberships

Connects user accounts with associations and manages membership validity and status.

Base endpoint:

```text
/api/memberships
```

Membership management requires the `MEMBERSHIP_MANAGE` permission. Users may view their own memberships, while access to other users or complete association membership lists requires authorization.

### Authorization

Provides:

- Role creation
- Permission creation
- Membership-role assignment
- Role-permission assignment
- Association-scoped permission evaluation

Base endpoint:

```text
/api/authorization
```

Authorization administration requires the `AUTHORIZATION_MANAGE` permission.

### Guardianships

Manages relationships between guardian and child user accounts.

Base endpoint:

```text
/api/guardianships
```

Supported statuses:

- `PENDING`
- `ACTIVE`
- `REJECTED`
- `EXPIRED`
- `TERMINATED`

Guardianship information is restricted to the guardian and child participating in the relationship. A user cannot create a guardianship request while claiming to be another guardian.

### Payments

Manages payment obligations and payment status history.

Base endpoint:

```text
/api/payments
```

Supported statuses:

- `OPEN`
- `PAID`
- `OVERDUE`
- `CANCELLED`

Payment creation and status management require the `PAYMENT_MANAGE` permission. Users may view their own payment information, while access to another user's payments requires appropriate authorization.

## Authorization Model

Authentication establishes the current user identity. Authorization then checks the user's active membership, association context, assigned roles and permissions.

Baseline permissions:

- `PAYMENT_MANAGE`
- `MEMBERSHIP_MANAGE`
- `AUTHORIZATION_MANAGE`

The backend returns:

- `401 Unauthorized` when authentication is missing
- `403 Forbidden` when the authenticated user lacks permission
- `404 Not Found` when a requested resource does not exist
- `409 Conflict` when a duplicate resource or assignment exists

UI visibility is not treated as authorization. The backend enforces access independently of the browser interface.

## Database Migrations

The PostgreSQL schema is managed by Flyway.

Migration files:

- `V1__create_sprint2_core_schema.sql`
- `V2__create_guardianship_relationships.sql`
- `V3__create_payment_obligations.sql`
- `V4__seed_authorization_permissions.sql`

The migrations create or configure:

- User accounts
- Associations
- Memberships
- Roles
- Permissions
- Membership-role assignments
- Role-permission assignments
- Guardianship relationships
- Payment obligations
- Payment status history
- Baseline authorization permissions

The V4 migration seeds the baseline permission catalogue and safely handles permission codes that already exist.

## Project Structure

- `Backend/`
  - `src/main/java/com/membernet/`
    - `association/`
    - `auth/`
    - `authorization/`
    - `config/`
    - `guardianship/`
    - `membership/`
    - `payment/`
    - `user/`
  - `src/main/resources/db/migration/`
  - `src/main/resources/static/`
  - `src/test/`
  - `Dockerfile`
  - `pom.xml`
- `docs/`
- `k8s/sprint2/`
- `.env.example`
- `.gitignore`
- `docker-compose.yml`
- `README.md`

## Environment Configuration

Create the local environment file:

```powershell
Copy-Item .\.env.example .\.env
notepad .\.env
```

Configure values similar to:

```dotenv
DB_PASSWORD=replace-with-a-secure-password
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=replace-with-a-secure-admin-password
ADMIN_FIRST_NAME=Admin
ADMIN_LAST_NAME=User
```

The `.env` file contains local secrets and must never be committed. It is excluded through `.gitignore`.

## Running Automated Tests

The automated integration tests require PostgreSQL with:

```text
Host: localhost
Port: 5433
Database: membernet_test
Username: postgres
```

From `Backend`, load the database password and datasource configuration:

```powershell
$dbPasswordLine = Get-Content ..\.env |
    Where-Object { $_ -like "DB_PASSWORD=*" } |
    Select-Object -First 1

$env:SPRING_DATASOURCE_URL =
    "jdbc:postgresql://localhost:5433/membernet_test"

$env:SPRING_DATASOURCE_USERNAME = "postgres"

$env:SPRING_DATASOURCE_PASSWORD =
    $dbPasswordLine.Substring("DB_PASSWORD=".Length)
```

Run the tests:

```powershell
mvn clean test
```

Verified result:

```text
Tests run: 32, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The test suite covers authentication, associations, memberships, authorization, guardianships, payments, validation and privacy boundaries.

## Running Locally

Open the backend directory:

```powershell
cd Backend
```

Load the database password:

```powershell
$dbPasswordLine = Get-Content ..\.env |
    Where-Object { $_ -like "DB_PASSWORD=*" } |
    Select-Object -First 1
```

Configure the application:

```powershell
$env:DB_URL =
    "jdbc:postgresql://localhost:5433/membernet_test"

$env:DB_USERNAME = "postgres"

$env:DB_PASSWORD =
    $dbPasswordLine.Substring("DB_PASSWORD=".Length)

$env:SESSION_PROTECTION_ENABLED = "true"
```

Start the application on the default port:

```powershell
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

To run on port `8084`:

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8084"
```

Then open:

```text
http://localhost:8084
```

## API Documentation

When the application runs on port `8080`, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

The OpenAPI document is available at:

```text
http://localhost:8080/v3/api-docs
```

When a different port is selected, replace `8080` with that port.

Swagger and the browser application use the same session cookie when opened under the same origin.

## Health Monitoring

General health:

```text
GET /actuator/health
```

Kubernetes readiness:

```text
GET /actuator/health/readiness
```

Kubernetes liveness:

```text
GET /actuator/health/liveness
```

Expected healthy response:

```json
{
  "status": "UP"
}
```

## Running with Docker Compose

Build the application JAR:

```powershell
cd Backend
mvn clean package
cd ..
```

Validate the Compose configuration:

```powershell
docker compose config --quiet
```

Start the services:

```powershell
docker compose up --build
```

Check the containers:

```powershell
docker compose ps
```

Follow the logs:

```powershell
docker compose logs -f
```

Stop the services:

```powershell
docker compose down
```

The PostgreSQL volume is preserved when the services are stopped. Do not use the `--volumes` option unless the stored database data should intentionally be removed.

## Kubernetes Deployment

Kubernetes manifests:

- `k8s/sprint2/membernet-sprint2.yaml`
- `k8s/sprint2/secret.example.yaml`

The real Kubernetes Secret must be created locally and must not be committed.

Create the namespace:

```powershell
kubectl create namespace membernet
```

Create or update the Secret from `.env`:

```powershell
kubectl create secret generic membernet-secrets `
    --namespace membernet `
    --from-env-file=.\.env `
    --dry-run=client `
    -o yaml |
kubectl apply -f -
```

Validate the manifest:

```powershell
kubectl apply --dry-run=client `
    -f .\k8s\sprint2\membernet-sprint2.yaml
```

Apply the deployment:

```powershell
kubectl apply `
    -f .\k8s\sprint2\membernet-sprint2.yaml
```

Wait for MemberNet:

```powershell
kubectl rollout status deployment/membernet `
    -n membernet `
    --timeout=180s
```

Check resources:

```powershell
kubectl get all -n membernet
kubectl get pvc -n membernet
```

Access MemberNet locally:

```powershell
kubectl port-forward `
    -n membernet `
    service/membernet `
    8081:8080
```

Open:

```text
http://localhost:8081
```

### Session scaling limitation

The current implementation stores authenticated sessions in application memory. The Kubernetes deployment therefore runs one MemberNet replica.

Multiple replicas require shared session storage, such as Spring Session with Redis, or another approved session-sharing solution.

## Security

Implemented security measures:

- BCrypt password hashing
- Session-based authentication
- Global session invalidation during logout
- Protected REST endpoints
- Backend authorization through memberships, roles and permissions
- Association-scoped permission evaluation
- Payment privacy between user accounts
- Membership privacy between user accounts and associations
- Guardianship access restricted to the guardian and child
- Generic invalid-credential responses
- HTTP `401` and `403` security responses
- Environment-based secret configuration
- Kubernetes Secret usage
- Non-root application container
- Disabled privilege escalation
- Read-only container root filesystem
- Dropped Linux capabilities
- Input validation
- Database foreign keys, unique constraints and check constraints
- Password hashes and credentials excluded from ordinary API responses

## Continuous Integration

The GitHub Actions workflow runs automatically for pushes and pull requests targeting the `sprint2` branch.

The workflow:

- Starts PostgreSQL 18
- Configures Java 21
- Runs `mvn clean test`
- Uploads Surefire test reports
- Displays the result through the README status badge

Workflow file:

```text
.github/workflows/backend-tests.yml
```

## AI Usage

AI assistance was used for:

- Requirements analysis
- Architecture planning
- Technology evaluation
- Implementation support
- Database modelling
- Flyway migration design
- Authorization design
- Debugging
- Test planning
- Docker and Kubernetes troubleshooting
- Documentation improvement

All AI-generated suggestions were manually reviewed and verified through compilation, automated testing, runtime testing and configuration validation.

## Documentation

Project documentation includes:

- `docs/ai-usage.md`
- `docs/architecture.md`
- `docs/assumptions-risks.md`
- `docs/development-log.md`
- `docs/requirements.md`
- `docs/technical-decisions.md`
- `docs/testing.md`
- `docs/sprint2/01-requirements-analysis.md`
- `docs/sprint2/02-architecture-design.md`
- `docs/sprint2/03-testing-and-deployment.md`
- `docs/sprint2/04-ai-assisted-development.md`
- `docs/sprint2/05-security-and-operations.md`
- `docs/sprint2/06-sprint2-summary.md`
- `docs/sprint2/07-api-usage-guide.md`
- `docs/sprint2/08-troubleshooting-guide.md`
- `docs/sprint2/09-final-verification-checklist.md`

## Verified Status

The current Sprint 2 implementation includes:

- Complete login, session and logout workflow
- Associations and memberships
- Contextual roles and permissions
- Backend authorization enforcement
- Guardianship relationship privacy
- Payment obligations and payment history
- PostgreSQL persistence
- Four Flyway migrations
- Functional browser interface
- Swagger and OpenAPI documentation
- 32 passing automated tests
- Docker Compose configuration
- Validated Kubernetes manifest
- Actuator health monitoring
- GitHub Actions continuous integration
- Updated implementation and operational documentation

Sprint 2 is an engineering and training baseline. It is not presented as a complete production release.
