# Sprint 2 AI-Assisted Development

## 1. Purpose

This document describes how AI assistance was used during MemberNet Sprint 2.

AI supported the development process, but all generated suggestions were reviewed, adapted and verified manually before being accepted.

## 2. Development Workflow

The following workflow was used:

1. Understand the Sprint 2 requirement
2. Divide the requirement into smaller modules
3. Ask AI for architecture and implementation suggestions
4. Review the generated solution
5. Adapt the code to the existing MemberNet project
6. Compile the application
7. Run automated tests
8. Test important workflows manually
9. Correct implementation or configuration problems
10. Update technical documentation
11. Commit verified changes to Git

AI output was treated as a proposal, not automatically as a correct final solution.

## 3. Requirements Analysis

AI assistance was used to divide Sprint 2 into the following functional areas:

- Associations
- Memberships
- Roles and permissions
- Guardianship relationships
- Payment obligations
- Session authentication
- PostgreSQL schema migrations
- OpenAPI documentation
- Docker deployment
- Kubernetes deployment
- Automated testing
- Technical documentation

Dependencies between the modules were identified before implementation.

For example:

- A membership requires an existing user account and association.
- A role belongs to an association.
- A role can contain multiple permissions.
- A guardianship connects two different user accounts.
- A payment obligation references a user and association.
- Protected API endpoints require an authenticated session.

## 4. Architecture Assistance

AI was used to propose a layered Spring Boot architecture with:

- REST controllers
- Application services
- Request and response records
- JPA entities
- Spring Data repositories
- Database migrations
- Validation
- Centralized error handling
- Integration tests

The proposed architecture was compared with the existing Sprint 1 structure before being implemented.

## 5. Database Design Assistance

AI helped design PostgreSQL tables and constraints for:

- Associations
- Memberships
- Roles
- Permissions
- Membership-role assignments
- Role-permission assignments
- Guardianship relationships
- Payment obligations
- Payment status history

Suggested database rules included:

- Primary keys
- Foreign keys
- Unique constraints
- Status constraints
- Positive payment amounts
- Valid date ranges
- Prevention of self-guardianship
- Relationship consistency

Flyway migrations were used so that schema changes remained versioned and reproducible.

## 6. Implementation Assistance

AI helped generate initial versions of:

- Entity classes
- Repository interfaces
- Service classes
- REST controllers
- Request models
- Response models
- Exceptions
- Validation rules
- Flyway migrations
- Test cases
- Docker configuration
- Kubernetes manifests

Generated code was adjusted to match:

- Existing package names
- Existing repository interfaces
- Existing database fields
- Existing API error format
- Java 21
- Spring Boot 3.5
- Current MemberNet naming conventions

## 7. Debugging Assistance

AI was used to interpret compiler, test, Docker and Kubernetes errors.

Examples included:

### Incorrect Java File Contents

Some generated Java files initially contained classes with names that did not match their filenames.

The compiler reported duplicate classes and incorrect public class filenames.

The files were corrected so that each public Java type matched its filename.

### Repository Interface Mismatches

Repository implementations initially contained methods that were missing from their interfaces.

The interfaces and implementations were compared and aligned.

### BCrypt Initialization Error

The administrator account was initially created with values passed to the entity constructor in the wrong order.

This stored incorrect data in the password hash column and caused BCrypt authentication warnings.

The constructor call was corrected, the invalid administrator record was removed, and a new account with a proper BCrypt hash was created.

### Frontend and Backend Contract Mismatch

The frontend expected fields such as:

```text
memberId
roles
```

The Sprint 2 authentication response returned:

```text
userAccountId
accountStatus
```

This caused the JavaScript error:

```text
Cannot read properties of undefined
```

The frontend was updated to match the actual backend response.

### Kubernetes Image Error

Kubernetes initially reported:

```text
ErrImageNeverPull
```

The deployment referenced a local image that was not available inside the Docker Desktop Kubernetes cluster.

The image was published to Docker Hub and the deployment was updated to use the published image with an appropriate pull policy.

### Kubernetes YAML Indentation Error

Incorrect indentation placed container fields under template metadata.

Kubernetes rejected the deployment because of unknown fields.

The YAML hierarchy was corrected and verified with:

```powershell
kubectl apply --dry-run=client
```

### Database Test Configuration

Automated tests initially failed when datasource environment variables were missing or incorrect.

The PostgreSQL test URL, username and password were configured explicitly before running Maven tests.

### Session Protection Registration

The session interceptor initially existed only as a planned component and was not registered with Spring MVC.

`WebConfig` was added to register the interceptor for `/api/**`, while authentication endpoints remained public.

## 8. Testing AI Suggestions

Every material AI-assisted implementation was checked using one or more of the following:

```powershell
mvn clean compile
mvn clean test
git diff --check
docker compose config
kubectl apply --dry-run=client
kubectl rollout status
kubectl get pods
```

The final automated test result was:

```text
Tests run: 23
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Manual session tests also verified:

- Unauthorized access returns HTTP 401
- Login creates an authenticated session
- Protected endpoints accept the authenticated session
- Logout invalidates the session
- The invalidated session is rejected

## 9. Security Review

AI-assisted security suggestions included:

- BCrypt password hashing
- Generic authentication failure messages
- Session protection for REST endpoints
- Environment-based credentials
- Exclusion of `.env` from Git
- Kubernetes Secrets
- Non-root container execution
- Read-only container root filesystem
- Disabled privilege escalation
- Dropped Linux capabilities

Passwords and sensitive values were not intentionally committed to Git.

Example files contain placeholder values only.

## 10. Human Review

Human review was required for:

- Confirming requirements
- Choosing the final architecture
- Reviewing generated code
- Checking file and class names
- Selecting environment configuration
- Running commands
- Reading error output
- Testing API behavior
- Verifying database records
- Reviewing Git changes
- Approving commits
- Confirming deployment health

AI did not independently deploy or approve the project.

## 11. Benefits

AI assistance provided benefits including:

- Faster initial implementation
- Faster explanation of compiler errors
- More consistent validation rules
- Additional test case suggestions
- Improved technical documentation
- Faster Docker and Kubernetes troubleshooting
- Better visibility of dependencies between modules

## 12. Limitations

AI-generated suggestions sometimes required correction because they could:

- Assume fields that did not exist
- Produce mismatched filenames and class names
- Use incorrect constructor argument order
- Generate invalid YAML indentation
- Assume an image was available to Kubernetes
- Omit environment configuration
- Mismatch frontend and backend response formats
- Suggest code that compiled but required runtime validation

For this reason, compilation alone was not treated as sufficient verification.

## 13. Final Evaluation

AI was useful as a development assistant throughout Sprint 2.

The successful process combined:

- AI-generated suggestions
- Human review
- Compiler feedback
- Automated testing
- Manual testing
- Database verification
- Docker validation
- Kubernetes validation
- Git version control

This approach produced a tested and documented Sprint 2 implementation while maintaining human control over all final technical decisions.
