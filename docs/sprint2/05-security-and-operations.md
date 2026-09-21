# Sprint 2 Security and Operations

## 1. Purpose

This document describes the security controls and operational configuration implemented in MemberNet Sprint 2.

## 2. Authentication

MemberNet authenticates users using:

- Login email
- Password
- PostgreSQL user account records
- BCrypt password hashes
- HTTP sessions

Plain-text passwords are not stored in the database.

Invalid login attempts return a generic response without revealing whether an account exists.

## 3. Session Protection

After successful login, the authenticated user is stored in the HTTP session.

Protected endpoints match:

```text
/api/**
```

Public authentication endpoints are:

```text
POST /api/auth/login
POST /api/auth/logout
GET  /api/auth/session
```

Unauthenticated access to protected endpoints returns:

```json
{
  "message": "Authentication is required."
}
```

with HTTP status `401 Unauthorized`.

Logout invalidates the current session.

## 4. Request Validation

Jakarta Validation is used to validate request data.

Examples include:

- Required fields
- Valid email addresses
- Positive payment amounts
- Required identifiers
- Accepted terms
- Valid dates
- Valid status values

Validation errors return HTTP `400 Bad Request` with a meaningful message.

## 5. Database Security

PostgreSQL security measures include:

- Primary keys
- Foreign keys
- Unique constraints
- Check constraints
- Restricted invalid status values
- Positive payment amount validation
- Relationship consistency
- Versioned Flyway migrations

The application does not embed the PostgreSQL password in source code.

## 6. Environment Variables

Sensitive configuration is provided through environment variables:

```text
DB_PASSWORD
ADMIN_EMAIL
ADMIN_PASSWORD
```

Datasource configuration uses:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

The `.env` file is excluded from Git.

The repository contains `.env.example` with placeholder values only.

## 7. Docker Security

The MemberNet container uses:

- A non-root user
- A fixed working directory
- Only the application JAR
- Environment-based credentials
- Port `8080`

The application runs as user ID:

```text
1001
```

## 8. Kubernetes Security Context

The Kubernetes application container uses:

```yaml
securityContext:
  allowPrivilegeEscalation: false
  runAsNonRoot: true
  runAsUser: 1001
  readOnlyRootFilesystem: true
  capabilities:
    drop:
      - ALL
```

A temporary writable volume is mounted at `/tmp`.

## 9. Kubernetes Secrets

Passwords are stored in a Kubernetes Secret named:

```text
membernet-secrets
```

The actual Secret is created locally from `.env`.

Only `secret.example.yaml` is committed, and it contains placeholders.

## 10. Kubernetes ConfigMap

Non-sensitive configuration is stored in:

```text
membernet-config
```

Examples include:

- Database URL
- Database username
- Administrator first name
- Administrator last name

## 11. Resource Management

The PostgreSQL and MemberNet containers define CPU and memory requests and limits.

This helps Kubernetes:

- Schedule pods
- Prevent unlimited resource consumption
- Maintain predictable operation

## 12. Health Monitoring

Spring Boot Actuator provides:

```text
/actuator/health
/actuator/health/readiness
/actuator/health/liveness
```

Kubernetes uses readiness and liveness probes to monitor application health.

PostgreSQL uses `pg_isready`.

## 13. Persistent Storage

PostgreSQL data is stored through a Kubernetes PersistentVolumeClaim.

The PVC uses:

```text
ReadWriteOnce
```

with requested storage of:

```text
1Gi
```

This preserves database data when a PostgreSQL pod is recreated.

## 14. Operational Commands

Check Kubernetes resources:

```powershell
kubectl get all -n membernet
```

Check persistent storage:

```powershell
kubectl get pvc -n membernet
```

Check application logs:

```powershell
kubectl logs `
  -n membernet `
  deployment/membernet `
  --tail=100
```

Check PostgreSQL logs:

```powershell
kubectl logs `
  -n membernet `
  deployment/postgres `
  --tail=100
```

Check rollout status:

```powershell
kubectl rollout status `
  deployment/membernet `
  -n membernet `
  --timeout=180s
```

## 15. Known Limitations

Current limitations include:

- Session state is stored by the application instance.
- Advanced distributed session storage is not implemented.
- Fine-grained endpoint permission enforcement can be extended.
- TLS termination is not included in the local Kubernetes setup.
- External secret management is not configured.
- Production database backup automation is not included.
- Rate limiting is not implemented.

## 16. Future Improvements

Possible improvements include:

- Spring Security integration
- Redis-backed distributed sessions
- Role-based endpoint authorization
- HTTPS ingress
- External secret management
- Automated PostgreSQL backups
- Audit logging
- Rate limiting
- CI/CD security scanning
- Network policies
