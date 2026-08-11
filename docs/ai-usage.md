# AI Usage

AI was used throughout the MemberNet Sprint 1 development
process.

## How AI Was Used

- Analysing project requirements
- Planning the layered architecture
- Comparing in-memory and PostgreSQL persistence
- Generating initial Java code
- Adding Spring Data JPA
- Introducing BCrypt password hashing
- Diagnosing compilation errors
- Reviewing Maven output
- Improving frontend text
- Planning tests
- Creating documentation

## Human Decisions

The trainee decided to:

- Use Java 21 and Spring Boot
- Use PostgreSQL instead of keeping mock accounts
- Use MEMBER and ADMIN roles
- Remove Kubernetes configuration from Sprint 1
- Keep Docker support
- Remove training credentials from the visible login page
- Use numeric member IDs
- Verify functionality using pgAdmin and browser tests

## Human Verification

AI suggestions were not accepted without verification. The
trainee:

- Reviewed generated code
- Executed Maven builds
- Checked PostgreSQL tables in pgAdmin
- Tested member and administrator login
- Tested role-based home pages
- Corrected syntax and configuration errors
- Updated user information in PostgreSQL

## Ethical Considerations

User passwords should not be exposed or stored as plain text.
Personal information should be limited to what the application
requires. AI suggestions may be inaccurate, so human review and
testing remain necessary.
