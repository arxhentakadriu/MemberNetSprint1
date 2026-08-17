# Project Requirements

This document maps the MemberNet Sprint 1 requirements to the final implementation.

## 1. Functional Requirements

| Requirement                            | Implementation                                                                          | Status    |
| -------------------------------------- | --------------------------------------------------------------------------------------- | --------- |
| Display a login page                   | Browser-based HTML login page served by Spring Boot                                     | Completed |
| Allow a user to enter credentials      | Username and password input fields                                                      | Completed |
| Authenticate the user                  | Spring service, PostgreSQL account lookup and BCrypt verification                       | Completed |
| Load authenticated account information | Display name, member ID and roles loaded from PostgreSQL                                | Completed |
| Determine user permissions             | MEMBER and ADMIN roles loaded from the database                                         | Completed |
| Display the appropriate home page      | Member home or Administrator dashboard selected according to roles                      | Completed |
| Confirm successful login               | Successful-login confirmation displayed in the browser                                  | Completed |
| Allow the user to log out              | Logout clears the browser-side account state and returns to Login                       | Completed |
| Demonstrate a complete workflow        | Login, authentication, account loading, permissions, home page and logout work together | Completed |

## 2. User Interface Requirements

| Requirement                   | Implementation                                                        | Status    |
| ----------------------------- | --------------------------------------------------------------------- | --------- |
| Present information clearly   | Simple layout with clear headings, labels and account details         | Completed |
| Provide intuitive navigation  | Login and Logout actions provide a direct workflow                    | Completed |
| Display meaningful errors     | Invalid credentials and validation errors are shown to the user       | Completed |
| Confirm successful operations | Successful login message is displayed                                 | Completed |
| Remain responsive             | Responsive HTML and CSS support normal desktop and mobile browser use | Completed |
| Provide browser-based access  | Application is available through `http://localhost:8080`              | Completed |
| Allow visual-design freedom   | Custom MemberNet styling was created with HTML and CSS                | Completed |

## 3. Technical Requirements

| Requirement                                          | Implementation                                                              | Status                |
| ---------------------------------------------------- | --------------------------------------------------------------------------- | --------------------- |
| Implement the application using Java                 | Java 21 backend                                                             | Completed             |
| Support browser-based access                         | Static HTML, CSS and JavaScript served by Spring Boot                       | Completed             |
| Communicate using REST principles where appropriate  | Authentication request handled by a REST controller using JSON              | Completed             |
| Support container-based deployment                   | Dockerfile and Docker Compose configuration                                 | Completed and tested  |
| Remain compatible with Kubernetes                    | Docker image, environment variables, port 8080 and Actuator health endpoint | Prepared for Sprint 2 |
| Follow layered software architecture                 | Presentation, controller, service, repository and database layers           | Completed             |
| Support future database integration                  | PostgreSQL integration was fully implemented in Sprint 1                    | Completed             |
| Support future monitoring and operational management | Spring Boot Actuator health endpoint                                        | Completed             |
| Provide persistent storage                           | PostgreSQL stores accounts, password hashes and roles                       | Completed             |
| Protect stored passwords                             | BCrypt password hashing                                                     | Completed             |
| Support repeatable builds                            | Maven dependency and build configuration                                    | Completed             |

## 4. Documentation Requirements

| Requirement                               | Implementation                                             | Status    |
| ----------------------------------------- | ---------------------------------------------------------- | --------- |
| Document important technical decisions    | `technical-decisions.md`                                   | Completed |
| Document architectural ideas              | `architecture.md`                                          | Completed |
| Record implementation notes               | `development-log.md`                                       | Completed |
| Record assumptions                        | `assumptions-risks.md`                                     | Completed |
| Identify project risks                    | `assumptions-risks.md`                                     | Completed |
| Record lessons learned                    | Development log, AI usage and complete project report      | Completed |
| Maintain documentation during development | Documents were updated when persistence and Docker changed | Completed |
| Provide setup instructions                | `README.md`                                                | Completed |
| Document testing                          | `testing.md`                                               | Completed |
| Document AI usage                         | `ai-usage.md`                                              | Completed |

## 5. Quality Requirements

| Requirement                | Implementation                                                     | Status    |
| -------------------------- | ------------------------------------------------------------------ | --------- |
| Readability                | Descriptive class names, package separation and documentation      | Completed |
| Maintainability            | Layered architecture and repository abstraction                    | Completed |
| Modularity                 | Authentication, configuration and user components are separated    | Completed |
| Consistency                | Consistent Java packages, REST flow and UI wording                 | Completed |
| Appropriate error handling | Central API error handler and meaningful browser messages          | Completed |
| Future extensibility       | Repository interface, Docker support and environment configuration | Completed |
| Automated verification     | Spring Boot, JUnit and Mockito controller tests                    | Completed |
| Build quality              | Maven build completes with no test failures                        | Completed |

## 6. AI Usage Requirements

| Requirement                    | Implementation                                                          | Status    |
| ------------------------------ | ----------------------------------------------------------------------- | --------- |
| Analyze requirements           | AI supported requirement interpretation and planning                    | Completed |
| Explore technologies           | Java, Spring Boot, PostgreSQL and Docker alternatives were discussed    | Completed |
| Compare alternatives           | In-memory storage and PostgreSQL persistence were compared              | Completed |
| Solve technical problems       | AI supported debugging of Java, database and Docker errors              | Completed |
| Review source code             | Generated and modified code was reviewed during development             | Completed |
| Improve documentation          | AI helped structure and update project documentation                    | Completed |
| Identify improvements          | Security, testing, monitoring and Sprint 2 improvements were identified | Completed |
| Preserve human decision-making | The trainee manually reviewed, executed and tested suggestions          | Completed |

## 7. Requirement Verification

The requirements were verified through:

* Maven compilation
* Automated controller tests
* Manual browser tests
* PostgreSQL inspection through pgAdmin
* MEMBER login testing
* ADMIN login testing
* Invalid-credentials testing
* Account-information verification
* Permission verification
* Logout testing
* Spring Boot Actuator health check
* Docker Compose execution
* Git and GitHub review

The final verified Maven result was:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## 8. Final Requirement Status

All required Sprint 1 functional requirements have been implemented.

The main technical requirements have also been completed through Java, Spring Boot, REST communication, PostgreSQL, layered architecture, Docker and health monitoring.
