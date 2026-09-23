# MemberNet Sprint 2 - Assumptions, Risks and Open Decisions

## 1. Purpose

This document records the assumptions, risks, mitigations and open design
decisions for MemberNet Sprint 2 RC2.

Sprint 2 is an engineering and training baseline. It is not intended to be
a complete production release.

## 2. Assumptions

### 2.1 User accounts

- A User Account represents one application identity.
- Login e-mail addresses are unique and case-insensitive.
- User Accounts may exist without Memberships.
- Passwords are stored only as BCrypt hashes.
- Bootstrap administrator credentials are supplied through environment
  variables or Kubernetes Secrets.
- Account status must be ACTIVE before login is accepted.

### 2.2 Groups and associations

- Group is the preferred user-facing term.
- Association is currently used in backend code and persistence.
- Group and Association represent the same organizational concept in the
  current Sprint 2 implementation.
- An Association has a stable UUID and may contain multiple Memberships.
- Business ID is optional, while name, short name, country code and e-mail
  are validated.

### 2.3 Memberships

- A Membership connects one User Account with one Association.
- A User Account may have zero or multiple Memberships.
- Membership status and validity dates belong to the relationship, not to
  the User Account.
- Duplicate Memberships for the same User Account and Association are
  rejected.
- A validity end date cannot be earlier than its start date.

### 2.4 Roles and permissions

- Roles belong to an Association context.
- Roles are assigned through Memberships.
- Permissions describe individual capabilities.
- A Role assigned to a Membership must belong to the same Association.
- The final production Role and Permission catalogue remains open.

### 2.5 Guardianship

- Guardianship connects two separate User Accounts.
- Guardian and child are relationship contexts, not different account types.
- A User Account cannot be its own guardian.
- Duplicate guardian-child relationships are rejected.
- Guardianship does not replace Membership, Role or Permission checks.
- Detailed approval and authorization rules remain future design work.

### 2.6 Payments

- MemberNet manages payment obligations and status history.
- MemberNet does not execute external banking transactions in Sprint 2.
- Banking credentials and payment-provider secrets are not stored.
- Amounts must be positive and currency codes are uppercase.
- A PAID obligation must include a payment timestamp.
- Payment references are unique.

### 2.7 Authentication

- Successful login creates a server-side authenticated HTTP session.
- Protected REST endpoints require a valid authenticated session.
- Global Logout invalidates the session and returns the browser to Login.
- Session expiry follows the configured application-server rules.
- Multi-factor authentication, external identity providers and SSO are
  outside the current scope.

### 2.8 Persistence

- PostgreSQL is the current relational database platform.
- Flyway manages database schema evolution.
- UUID values are used as physical identifiers.
- Database constraints protect mandatory relationships and invalid states.
- PostgreSQL is an implementation decision, not a permanent business rule.

### 2.9 Deployment

- Local development may run through Maven or Docker Compose.
- Kubernetes is used as the Sprint 2 container-orchestration demonstration.
- Secrets are supplied externally and are not committed to Git.
- The final production hosting platform and topology remain open.

## 3. Risks and mitigations

### 3.1 Unauthorized cross-group access

**Risk:** A user with Memberships in several Associations could access data
from the wrong Association.

**Mitigation:** Carry Association context through Memberships, Roles and
Permissions, and enforce access in the backend rather than only hiding user
interface controls.

### 3.2 Incomplete authorization enforcement

**Risk:** Authentication may protect an endpoint while still allowing an
authenticated user to perform an operation without the required Permission.

**Mitigation:** Add operation-level authorization checks and automated tests
for allowed and denied requests.

### 3.3 Exposed credentials

**Risk:** Database or administrator passwords could be committed to Git,
printed in logs or included in documentation.

**Mitigation:** Use environment variables, `.env` files excluded by
`.gitignore`, Docker configuration and Kubernetes Secrets. Example files use
placeholders only.

### 3.4 Invalid or inconsistent data

**Risk:** Duplicate Memberships, Guardianships, payment references or invalid
validity periods could create inconsistent business information.

**Mitigation:** Apply request validation, service-level rules, unique
constraints, foreign keys and automated tests.

### 3.5 Session misuse

**Risk:** Protected resources could remain available after logout or be
accessed without authentication.

**Mitigation:** Invalidate the HTTP session during logout and use a backend
interceptor for protected API routes. Test access before login and after
logout.

### 3.6 Sensitive logging

**Risk:** Passwords, hashes, personal information or payment-related data
could appear in logs.

**Mitigation:** Do not log credentials or complete sensitive payloads.
Production logging configuration requires a separate security review.

### 3.7 Frontend and API inconsistency

**Risk:** The user interface may show controls or terminology that do not
match the REST API and business model.

**Mitigation:** Use Group in user-facing text, keep Association documented as
the backend term, and evolve the UI together with OpenAPI and implementation.

### 3.8 Migration failure

**Risk:** Application startup could fail when the database schema and code do
not match.

**Mitigation:** Keep Flyway migrations immutable after deployment, validate
them during startup and execute automated tests against PostgreSQL.

### 3.9 Container and port conflicts

**Risk:** Local services, Kubernetes port-forwarding and Docker containers
may attempt to use the same port.

**Mitigation:** Document configurable ports, inspect active processes and
stop unused development services before startup.

### 3.10 AI-generated defects

**Risk:** AI-generated code or documentation may introduce incorrect
assumptions, duplicate files or inconsistent behavior.

**Mitigation:** Review every change, run automated tests, inspect Git diffs
and verify decisions against the Sprint 2 source material.

## 4. Open design decisions

The following items are intentionally not fixed permanently by Sprint 2:

- Final distinction between the terms Group and Association
- Final Role and Permission catalogue
- Detailed active Group storage and transport mechanism
- Full Guardianship approval and termination workflow
- Payment-provider selection and reconciliation
- Production identity provider and authentication technology
- Production deployment platform and scaling topology
- Final session timeout and invalidation policies
- Complete frontend framework and component structure
- Final backup, monitoring and disaster-recovery procedures

## 5. Acceptance evidence

Sprint 2 completion is demonstrated through:

- PostgreSQL and Flyway migrations
- REST APIs using DTOs and validation
- OpenAPI/Swagger documentation
- Session authentication and Global Logout
- Automated controller and integration tests
- GitHub Actions execution
- Docker Compose and Kubernetes manifests
- Security and deployment documentation
- Manual verification of login, protected access and logout
- Verification of Association, Membership, Authorization, Guardianship and
  Payment operations

## 6. Review rule

When an assumption becomes a confirmed decision, update this document, the
architecture documentation, implementation, database model, API
documentation and relevant tests together.
