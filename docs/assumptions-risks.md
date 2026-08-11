# Assumptions and Risks

## Assumptions

- Accounts exist before login.
- Registration is outside Sprint 1.
- MEMBER and ADMIN are sufficient for the current scope.
- PostgreSQL is available on the configured host and port.
- Kubernetes deployment belongs to Sprint 2.

## Risks

### Exposed Database Credentials

Database passwords must not be committed to source control.
Environment variables should be used.

### Demonstration Passwords

Simple demonstration passwords must not be used in production.

### Browser Session Storage

Client-side session data is suitable for this training project
but should be replaced with secure server-side authentication
for production.

### Database Availability

Authentication cannot work when PostgreSQL is unavailable.

### AI-Generated Errors

AI-generated code may contain incorrect assumptions or security
issues. Every change must be reviewed and tested manually.
