# Testing

| Test                | Input                    | Expected Result                   |
| ------------------- | ------------------------ | --------------------------------- |
| Member login        | Valid MEMBER credentials | Member home displayed             |
| Admin login         | Valid ADMIN credentials  | Administrator dashboard displayed |
| Invalid password    | Wrong password           | Meaningful error displayed        |
| Unknown username    | Non-existing username    | Meaningful error displayed        |
| Empty username      | Empty field              | Validation error displayed        |
| Empty password      | Empty field              | Validation error displayed        |
| Account information | Successful login         | Name and member ID displayed      |
| Permissions         | ADMIN account            | ADMIN and MEMBER displayed        |
| Logout              | Click Logout             | Return to Login page              |
| Database connection | Start application        | PostgreSQL connection succeeds    |
| Health endpoint     | `/actuator/health`       | Status `UP`                       |
| Maven build         | `mvn clean install`      | Build succeeds                    |

## Automated Testing

Controller tests are implemented with Spring Boot Test, JUnit
and Mockito.

## Build Result

The Maven build completed successfully with no failed tests.
