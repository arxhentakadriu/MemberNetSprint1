# Testing

This document describes the tests performed for MemberNet Sprint 1.

## Test Strategy

Testing includes:

- Automated controller tests.
- Manual browser testing.
- PostgreSQL connection testing.
- Docker container testing.
- REST API testing.
- Application health monitoring.
- Maven build verification.

## Functional Test Cases

| Test | Input or Action | Expected Result | Status |
|---|---|---|---|
| Member login | Enter valid MEMBER credentials | Member home page is displayed | Passed |
| Administrator login | Enter valid ADMIN credentials | Administrator dashboard is displayed | Passed |
| Invalid password | Enter an incorrect password | Meaningful authentication error is displayed | Passed |
| Unknown username | Enter a username that does not exist | Meaningful authentication error is displayed | Passed |
| Empty username | Submit the form without a username | Validation error is displayed | Passed |
| Empty password | Submit the form without a password | Validation error is displayed | Passed |
| Account information | Complete a successful login | Display name and member ID are displayed | Passed |
| Member permissions | Log in with a MEMBER account | MEMBER permission is displayed | Passed |
| Administrator permissions | Log in with an ADMIN account | ADMIN and MEMBER permissions are displayed | Passed |
| Role-based page | Log in using different account roles | The correct home page is selected | Passed |
| Login confirmation | Complete a successful login | “Login completed successfully” is displayed | Passed |
| Logout | Select the Logout button | User data is cleared and the Login page is displayed | Passed |
| Database connection | Start the application | Connection to PostgreSQL succeeds | Passed |
| Persistent account loading | Restart the application and log in | Account is loaded again from PostgreSQL | Passed |
| Health endpoint | Open `/actuator/health` | Response contains `"status": "UP"` | Passed |
| Responsive interface | Open the page at different browser widths | Interface remains readable and usable | Passed |
| Maven build | Run `mvn clean install` | Build and automated tests succeed | Passed |
| Docker startup | Run `docker compose up --build` | Application and PostgreSQL containers start | Passed |
| Docker login | Open the containerized application and log in | Authentication works with containerized PostgreSQL | Passed |

## Automated Testing

Automated controller tests are implemented using:

- JUnit.
- Spring Boot Test.
- MockMvc.
- Mockito where dependency isolation is required.

The automated tests verify the REST authentication endpoint for scenarios such as:

- Successful authentication.
- Invalid credentials.
- Correct HTTP response status.
- Correct response content.

The tests can be executed from the `Backend` directory with:

```powershell
mvn test
