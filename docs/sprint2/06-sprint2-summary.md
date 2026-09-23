# MemberNet Sprint 2 Summary

## 1. Sprint Objective

The objective of Sprint 2 was to extend MemberNet from a basic authentication application into a broader membership management system.

Sprint 2 also introduced production-oriented database management, API documentation and Kubernetes deployment.

## 2. Implemented Modules

### User Accounts and Authentication

- PostgreSQL user accounts
- BCrypt password hashing
- Email-based login
- HTTP sessions
- Current-session endpoint
- Logout and session invalidation
- Protection of REST endpoints

### Associations

- Association creation
- Association retrieval
- Unique short names
- Unique business identifiers
- Terms acceptance
- Association status

### Memberships

- User-to-association membership
- Membership status
- Validity dates
- Duplicate prevention
- User and association validation

### Authorization

- Association-specific roles
- Global permissions
- Role-permission relationships
- Membership-role relationships
- Database consistency constraints

### Guardianships

- Guardian and child relationships
- Relationship status
- Validity dates
- Self-relationship prevention
- Duplicate prevention

### Payments

- Payment obligations
- Positive monetary amounts
- Currency codes
- Payment references
- Due dates
- Payment statuses
- Paid timestamps
- Payment status history

## 3. Database Implementation

Flyway manages the Sprint 2 PostgreSQL schema.

Implemented migrations:

```text
V1__create_sprint2_core_schema.sql
V2__create_guardianship_relationships.sql
V3__create_payment_obligations.sql
```

The schema uses foreign keys, unique constraints, indexes and check constraints to protect data integrity.

## 4. REST API

The application exposes REST endpoints for:

- Authentication
- Associations
- Memberships
- Authorization
- Guardianships
- Payments

Requests use JSON and return meaningful HTTP status codes and error messages.

## 5. API Documentation

Springdoc OpenAPI generates API documentation automatically.

Swagger UI:

```text
/swagger-ui.html
```

OpenAPI specification:

```text
/v3/api-docs
```

## 6. Testing Result

The final verified Maven test result was:

```text
Tests run: 32
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Tests cover authentication, associations, memberships, authorization, guardianships, payments, API documentation and session protection.

## 7. Docker Deployment

Docker Compose runs:

- MemberNet
- PostgreSQL 18
- Database health checks
- Persistent database storage
- Environment-based configuration

The MemberNet image is built from the executable Spring Boot JAR.

## 8. Kubernetes Deployment

The Kubernetes deployment includes:

- MemberNet namespace
- ConfigMap
- Secret reference
- PostgreSQL Deployment
- PostgreSQL Service
- PersistentVolumeClaim
- MemberNet Deployment
- MemberNet Service
- Resource requests and limits
- Readiness probes
- Liveness probes
- Container security context

The verified deployment contained two healthy MemberNet pods and one healthy PostgreSQL pod.

## 9. Problems Solved

Important problems solved during Sprint 2 included:

- Incorrect Java class filenames
- Duplicate Java classes
- Repository interface mismatches
- Invalid BCrypt data
- Frontend and backend response mismatch
- PostgreSQL password configuration
- Kubernetes image availability
- Kubernetes YAML indentation
- Kubernetes secret configuration
- Session interceptor registration
- Port conflicts during local testing

## 10. AI-Assisted Process

AI was used for:

- Requirements analysis
- Architecture proposals
- Code generation support
- Database schema design
- Debugging
- Test design
- Docker troubleshooting
- Kubernetes troubleshooting
- Documentation

All important suggestions were reviewed and tested manually.

## 11. Sprint Deliverables

Sprint 2 deliverables include:

- Java and Spring Boot backend
- Browser-based frontend
- PostgreSQL schema
- Flyway migrations
- Automated tests
- Swagger/OpenAPI documentation
- Dockerfile
- Docker Compose configuration
- Kubernetes manifests
- Environment variable examples
- Technical documentation
- AI usage documentation
- Git commit history

## 12. Final Status

MemberNet Sprint 2 is:

- Compiling successfully
- Passing all automated tests
- Running with PostgreSQL
- Available through Docker
- Deployable to Kubernetes
- Documented through Swagger
- Protected with authenticated sessions
- Stored in the `sprint2` Git branch

## 13. Possible Next Steps

Future development may include:

- Spring Security
- Fine-grained endpoint authorization
- User registration
- Account administration
- Redis-backed sessions
- Audit logs
- Payment provider integration
- Email notifications
- CI/CD pipelines
- Kubernetes ingress and TLS
- Production monitoring
