# Sprint 2 Testing and Deployment

## 1. Purpose

This document describes how MemberNet Sprint 2 was tested and deployed using Maven, PostgreSQL, Docker Compose and Kubernetes.

The objective was to verify that the application:

- Compiles successfully
- Connects to PostgreSQL
- Applies Flyway migrations
- Validates API requests
- Protects REST endpoints
- Maintains authenticated sessions
- Runs inside Docker containers
- Deploys successfully to Kubernetes
- Provides health, readiness and liveness endpoints

## 2. Test Environment

The verified development environment included:

- Windows 11
- Java 21
- Maven 3.9
- Spring Boot 3.5
- PostgreSQL 18
- Docker Desktop
- Docker Compose
- Docker Desktop Kubernetes
- kubectl
- PowerShell

## 3. PostgreSQL Test Database

Automated integration tests use a PostgreSQL database available locally on port `5433`.

Example datasource configuration:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5433/membernet_test"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
```

Database passwords are loaded from local environment variables and are not committed to Git.

## 4. Automated Tests

Tests are implemented with:

- JUnit 5
- Spring Boot Test
- MockMvc
- PostgreSQL
- Transaction rollback

Run the complete test suite from the `Backend` directory:

```powershell
mvn clean test
```

Verified result:

```text
Tests run: 23
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## 5. Tested Modules

### Authentication

Authentication tests verify:

- Successful login
- Invalid credentials
- Invalid email validation
- Current authenticated session
- Unauthenticated session rejection
- Successful logout
- Session invalidation

### Session Protection

Session protection tests verify:

- Protected endpoints reject unauthenticated requests
- Unauthorized requests return HTTP `401`
- The response contains a meaningful message
- Public OpenAPI documentation remains accessible

Example unauthorized response:

```json
{
  "message": "Authentication is required."
}
```

### Associations

Association tests verify:

- Valid association creation
- Required fields
- Terms acceptance
- Duplicate values
- Correct HTTP status codes

### Memberships

Membership tests verify:

- Valid membership creation
- Association and user validation
- Duplicate membership prevention
- Membership status and validity

### Authorization

Authorization tests verify:

- Role creation
- Permission creation
- Role-permission assignment
- Membership-role assignment
- Invalid and duplicate assignments

### Guardianships

Guardianship tests verify:

- Valid guardian-child relationship creation
- Different guardian and child accounts
- Duplicate relationship prevention
- Validity dates
- Status handling

### Payments

Payment tests verify:

- Payment obligation creation
- Positive payment amounts
- User, association and membership references
- Payment status changes
- Paid timestamp handling
- Payment status history

## 6. Manual Session Test

The application was started locally with session protection enabled:

```powershell
$env:SESSION_PROTECTION_ENABLED = "true"
mvn spring-boot:run
```

An unauthenticated request to a protected endpoint returned HTTP `401`:

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:8080/api/associations"
```

Login was performed while storing the HTTP session:

```powershell
$body = @{
    loginEmail = "admin@example.com"
    password   = "LOCAL_TEST_PASSWORD"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/api/auth/login" `
  -ContentType "application/json" `
  -Body $body `
  -SessionVariable membernetSession
```

The protected endpoint was then accessible with the authenticated session:

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:8080/api/associations" `
  -WebSession $membernetSession
```

After logout, the same session was rejected again:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/api/auth/logout" `
  -WebSession $membernetSession
```

This verified login, authorization, logout and session invalidation.

## 7. Flyway Migration Verification

Flyway automatically validates and applies the database migrations when the application starts.

The current migrations are:

```text
V1__create_sprint2_core_schema.sql
V2__create_guardianship_relationships.sql
V3__create_payment_obligations.sql
```

Successful startup confirms that:

- Migration checksums are valid
- Migration ordering is valid
- Database constraints are accepted
- The schema is compatible with the JPA entities

## 8. Swagger and OpenAPI Verification

Swagger UI was verified at:

```text
http://localhost:8080/swagger-ui.html
```

The generated OpenAPI specification was verified at:

```text
http://localhost:8080/v3/api-docs
```

Swagger displays endpoints for the Sprint 2 modules and allows requests to be tested through the browser.

## 9. Docker Deployment

The executable JAR is built using:

```powershell
cd Backend
mvn clean package
cd ..
```

Docker Compose configuration is validated using:

```powershell
docker compose config
```

The services are started using:

```powershell
docker compose up --build
```

Docker Compose runs:

- The MemberNet Spring Boot application
- PostgreSQL 18
- A persistent PostgreSQL volume
- A database health check
- Environment-based configuration

Service status is checked using:

```powershell
docker compose ps
```

Logs are inspected using:

```powershell
docker compose logs -f
```

## 10. Docker Image

The Sprint 2 image is built using:

```powershell
docker build `
  -t arxhenta/membernet-sprint2:latest `
  .\Backend
```

The image can be published using:

```powershell
docker push arxhenta/membernet-sprint2:latest
```

No passwords or environment files are included in the image.

## 11. Kubernetes Validation

Kubernetes manifests are validated before deployment:

```powershell
kubectl apply --dry-run=client `
  -f .\k8s\sprint2\membernet-sprint2.yaml
```

The secret example is also validated:

```powershell
kubectl apply --dry-run=client `
  -f .\k8s\sprint2\secret.example.yaml
```

The example secret contains placeholders only.

## 12. Kubernetes Deployment

The actual Kubernetes secret is created locally from `.env`:

```powershell
kubectl create secret generic membernet-secrets `
  --namespace membernet `
  --from-env-file=.\.env `
  --dry-run=client `
  -o yaml |
kubectl apply -f -
```

The application resources are deployed using:

```powershell
kubectl apply `
  -f .\k8s\sprint2\membernet-sprint2.yaml
```

The deployment rollout is verified using:

```powershell
kubectl rollout status deployment/membernet `
  -n membernet `
  --timeout=180s
```

## 13. Verified Kubernetes State

The successful deployment contained:

- Two running MemberNet pods
- One running PostgreSQL pod
- A MemberNet ClusterIP service
- A PostgreSQL ClusterIP service
- A bound PostgreSQL persistent volume claim
- Kubernetes ConfigMap configuration
- Kubernetes Secret configuration

Resources are checked using:

```powershell
kubectl get all -n membernet
kubectl get pvc -n membernet
```

## 14. Local Kubernetes Access

Because the MemberNet service uses `ClusterIP`, local access is provided through port forwarding:

```powershell
kubectl port-forward `
  -n membernet `
  service/membernet `
  8081:8080
```

The application is then accessible at:

```text
http://localhost:8081
```

The port-forward terminal must remain open while the application is being accessed.

## 15. Health Probes

Kubernetes uses Spring Boot Actuator endpoints:

```text
/actuator/health/readiness
/actuator/health/liveness
```

Readiness prevents traffic from reaching an application pod before it is ready.

Liveness allows Kubernetes to restart an unhealthy application container.

PostgreSQL health is checked with `pg_isready`.

## 16. Security Verification

The deployed application uses:

- Non-root execution
- Disabled privilege escalation
- Read-only root filesystem
- Dropped Linux capabilities
- Temporary writable `/tmp` volume
- Kubernetes Secrets
- Environment-based database credentials
- Session-protected REST endpoints
- BCrypt password hashing

## 17. Final Result

MemberNet Sprint 2 was successfully:

- Compiled
- Tested
- Connected to PostgreSQL
- Migrated with Flyway
- Documented with OpenAPI
- Packaged with Docker
- Deployed to Kubernetes
- Verified with health probes
- Protected with authenticated HTTP sessions
