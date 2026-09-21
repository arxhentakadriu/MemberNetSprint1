# MemberNet Sprint 2

[![Backend Tests](https://github.com/arxhentakadriu/MemberNetSprint1/actions/workflows/backend-tests.yml/badge.svg?branch=sprint2)](https://github.com/arxhentakadriu/MemberNetSprint1/actions/workflows/backend-tests.yml)

MemberNet is a browser-based membership management application developed as part of the MemberNet AI Developer Training Project.

Sprint 2 extends the authentication workflow from Sprint 1 with association management, memberships, authorization, guardianship relationships, payment obligations, API documentation, session protection, Docker Compose and Kubernetes deployment.

## Features

## Features

- Login with email and password
- BCrypt password hashing
- Session-based authentication
- Protected REST API endpoints
- Logout and session invalidation
- User account persistence
- Association management
- Membership management
- Roles and permissions
- Membership-role assignment
- Guardianship relationships
- Payment obligations
- Payment status history
- Request validation and consistent API errors
- PostgreSQL persistence
- Flyway database migrations
- Swagger/OpenAPI documentation
- Spring Boot Actuator health probes
- Docker and Docker Compose support
- Kubernetes deployment configuration
- Automated integration tests

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
- Git and GitHub

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
- **Controller layer:** REST endpoints and request validation
- **Service layer:** business rules and workflow processing
- **Repository layer:** Spring Data JPA database access
- **Database layer:** PostgreSQL schema managed by Flyway
- **Infrastructure layer:** Docker, Kubernetes and Actuator

## Main Modules

### Authentication

Provides:

- Login through `POST /api/auth/login`
- Current session through `GET /api/auth/session`
- Logout through `POST /api/auth/logout`
- Session-based protection for `/api/**` endpoints
- BCrypt password verification

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

### Authorization

Provides:

- Roles
- Permissions
- Role-permission assignment
- Membership-role assignment

Base endpoint:

```text
/api/authorization
```

### Guardianships

Manages relationships between guardian and child user accounts.

Base endpoint:

```text
/api/guardianships
```

Supported statuses include:

- `PENDING`
- `ACTIVE`
- `REJECTED`
- `EXPIRED`
- `TERMINATED`

### Payments

Manages payment obligations and payment status history.

Base endpoint:

```text
/api/payments
```

Supported statuses include:

- `OPEN`
- `PAID`
- `OVERDUE`
- `CANCELLED`

## Database Migrations

The PostgreSQL schema is managed by Flyway.

Migration files:

```text
Backend/src/main/resources/db/migration/
├── V1__create_sprint2_core_schema.sql
├── V2__create_guardianship_relationships.sql
└── V3__create_payment_obligations.sql
```

The migrations create the core tables for:

- User accounts
- Associations
- Memberships
- Roles
- Permissions
- Membership roles
- Role permissions
- Guardianship relationships
- Payment obligations
- Payment status history

## Project Structure

```text
MemberNet-Sprint1/
├── Backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/membernet/
│   │   │   │   ├── association/
│   │   │   │   ├── auth/
│   │   │   │   ├── authorization/
│   │   │   │   ├── config/
│   │   │   │   ├── guardianship/
│   │   │   │   ├── membership/
│   │   │   │   ├── payment/
│   │   │   │   ├── user/
│   │   │   │   └── MemberNetApplication.java
│   │   │   └── resources/
│   │   │       ├── db/migration/
│   │   │       ├── static/
│   │   │       ├── application.properties
│   │   │       └── application.yml
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── docs/
│   └── sprint2/
├── k8s/
│   └── sprint2/
├── .env.example
├── .gitignore
├── docker-compose.yml
└── README.md
```

## Environment Configuration

Create the local environment file from the example:

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

Never commit `.env`. It is excluded through `.gitignore`.

## Running Automated Tests

Start the PostgreSQL test container if it is not already running.

From `Backend`, configure the test datasource:

```powershell
$dbPasswordLine = Get-Content ..\.env |
    Where-Object { $_ -like "DB_PASSWORD=*" } |
    Select-Object -First 1

$env:DB_PASSWORD = $dbPasswordLine.Substring("DB_PASSWORD=".Length)
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5433/membernet_test"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
```

Run:

```powershell
mvn clean test
```

Verified result:

```text
Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Running Locally

Open the backend directory:

```powershell
cd Backend
```

Set the required environment variables:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5433/membernet_test"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
$env:SESSION_PROTECTION_ENABLED = "true"
```

Start the application:

```powershell
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

## API Documentation

When the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

The OpenAPI document is available at:

```text
http://localhost:8080/v3/api-docs
```

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
docker compose config
```

Start the services:

```powershell
docker compose up --build
```

Check their status:

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

The PostgreSQL volume is preserved unless volumes are explicitly removed.

## Kubernetes Deployment

Kubernetes manifests are located in:

```text
k8s/sprint2/
├── membernet-sprint2.yaml
└── secret.example.yaml
```

The real secret is created locally from `.env` and must not be committed.

Create the namespace:

```powershell
kubectl create namespace membernet
```

Create or update the secret:

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

Apply it:

```powershell
kubectl apply `
  -f .\k8s\sprint2\membernet-sprint2.yaml
```

Wait for the application rollout:

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

Access the application locally:

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

## Security

Implemented security measures include:

- BCrypt password hashing
- Session-based authentication
- Protected REST endpoints
- Generic invalid-credential responses
- Environment-based secret configuration
- Kubernetes Secret usage
- Non-root application container
- Disabled privilege escalation
- Read-only container root filesystem
- Dropped Linux capabilities
- Input validation
- Database constraints

## AI Usage

AI assistance was used for:

- Requirements analysis
- Architecture planning
- Technology evaluation
- Implementation support
- Database modelling
- Flyway migration design
- Debugging
- Test planning
- Docker and Kubernetes troubleshooting
- Documentation improvement

All generated suggestions were manually reviewed and verified through compilation, automated tests and runtime testing.

## Documentation

Project documentation is stored under:

```text
docs/
├── ai-usage.md
├── architecture.md
├── assumptions-risks.md
├── development-log.md
├── requirements.md
├── technical-decisions.md
├── testing.md
└── sprint2/
    ├── 01-requirements-analysis.md
    └── 02-architecture-design.md
```

## Current Status

Sprint 2 currently includes:

- Complete authentication and session workflow
- Associations and memberships
- Roles and permissions
- Guardianship relationships
- Payment obligations and history
- PostgreSQL and Flyway migrations
- Swagger/OpenAPI documentation
- 23 passing automated tests
- Docker Compose deployment
- Kubernetes deployment
- Health monitoring
- GitHub version control
