# Sprint 2 Final Verification Checklist

## Source Code

- [ ] Java source code compiles
- [ ] Public Java types match their filenames
- [ ] No duplicate classes exist
- [ ] Controllers delegate business logic to services
- [ ] Repositories handle database access
- [ ] Request validation is enabled
- [ ] API errors use a consistent response format

## Database

- [ ] PostgreSQL starts successfully
- [ ] Flyway validates all migrations
- [ ] V1 migration creates the core schema
- [ ] V2 migration creates guardianships
- [ ] V3 migration creates payments
- [ ] Foreign keys are valid
- [ ] Unique constraints are valid
- [ ] Check constraints are valid
- [ ] Administrator password is stored as BCrypt

## Authentication

- [ ] Valid user can log in
- [ ] Invalid password is rejected
- [ ] Invalid email is rejected
- [ ] Successful login creates a session
- [ ] Current session can be retrieved
- [ ] Protected endpoints reject unauthenticated requests
- [ ] Authenticated requests access protected endpoints
- [ ] Logout succeeds
- [ ] Logged-out session is rejected

## Functional Modules

- [ ] Associations work
- [ ] Memberships work
- [ ] Roles work
- [ ] Permissions work
- [ ] Membership-role assignment works
- [ ] Role-permission assignment works
- [ ] Guardianships work
- [ ] Payment obligations work
- [ ] Payment status history works

## Tests

- [x] `mvn clean test` completes successfully
- [x] 32 tests run
- [x] 0 failures
- [x] 0 errors
- [x] 0 skipped tests
- [x] Manual session workflow is verified

## API Documentation

- [ ] Swagger UI opens
- [ ] `/v3/api-docs` returns OpenAPI JSON
- [ ] Sprint 2 controllers are displayed
- [ ] Request schemas are displayed
- [ ] Response schemas are displayed

## Docker

- [ ] JAR builds successfully
- [ ] Docker image builds successfully
- [ ] Docker Compose validates successfully
- [ ] PostgreSQL becomes healthy
- [ ] MemberNet starts successfully
- [ ] Application health returns `UP`
- [ ] `.env` is not committed
- [ ] Docker image contains no secrets

## Kubernetes

- [ ] Docker Desktop Kubernetes is running
- [ ] `docker-desktop` context is active
- [ ] Kubernetes manifest passes dry-run
- [ ] Namespace exists
- [ ] Secret exists
- [ ] ConfigMap exists
- [ ] PostgreSQL PVC is bound
- [ ] PostgreSQL pod is ready
- [ ] Two MemberNet pods are ready
- [ ] MemberNet rollout completes
- [ ] Readiness probe succeeds
- [ ] Liveness probe succeeds
- [ ] Port forwarding works
- [ ] Browser login works through Kubernetes

## Security

- [ ] BCrypt is used
- [ ] Session protection is enabled
- [ ] Generic invalid-credential messages are used
- [ ] Environment variables contain secrets
- [ ] Kubernetes Secret contains deployment secrets
- [ ] Example secret contains placeholders only
- [ ] Container runs as non-root
- [ ] Privilege escalation is disabled
- [ ] Root filesystem is read-only
- [ ] Linux capabilities are dropped

## Documentation

- [ ] README describes Sprint 2
- [ ] Requirements are documented
- [ ] Architecture is documented
- [ ] Testing and deployment are documented
- [ ] AI-assisted development is documented
- [ ] Security and operations are documented
- [ ] Sprint summary is documented
- [ ] Troubleshooting is documented
- [ ] Commands do not contain real passwords

## Git

- [ ] Work is committed on branch `sprint2`
- [ ] `git diff --check` reports no errors
- [ ] `git status` is clean
- [ ] Branch is pushed to GitHub
- [ ] `.env` is ignored
- [ ] Kubernetes real secrets are not committed
- [ ] Commit messages describe the changes

## Final Result

When every applicable item is checked, Sprint 2 is ready for final review.
