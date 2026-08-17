# Development Log

This document records the main activities, decisions, problems, solutions and verification steps completed during MemberNet Sprint 1.

## Step 1 - Requirements Analysis

The functional, user interface, technical, documentation, quality and AI-usage requirements were reviewed.

The required authentication workflow was identified as:

```
Login page
    |
Enter credentials
    |
Authenticate user
    |
Load account information
    |
Determine permissions
    |
Display role-based home page
    |
Confirm successful login
    |
Logout
```

The analysis confirmed that user registration, password reset and full member management were outside the Sprint 1 scope.

AI was used to explain the requirements, identify the required components and create an initial development plan.

## Step 2 - Technology Evaluation

Different implementation options were considered.

The selected technologies were:

* Java 21
* Spring Boot 3.5
* Maven
* HTML
* CSS
* JavaScript
* PostgreSQL 18
* Spring Data JPA
* BCrypt
* Docker
* Docker Compose
* Spring Boot Actuator
* JUnit
* Mockito
* Git and GitHub

Java 21 was selected because it is a modern Long-Term Support version.

Spring Boot was selected because it provides REST support, validation, dependency injection, database integration, testing and operational endpoints.

Maven was selected for dependency management, compilation, automated testing and executable JAR creation.

PostgreSQL was selected for persistent relational storage.

## Step 3 - Initial Project Structure

The Spring Boot backend was organized into the following packages:

```
com.membernet.auth
com.membernet.config
com.membernet.user
```

The main application class was:

```
MemberNetApplication
```

The frontend files were placed under:

```
src/main/resources/static
```

The project followed a layered architecture:

```
Presentation
Controller
Service
Repository
Database
```

Documentation files were created in the `docs` directory so that documentation could evolve together with the implementation.

## Step 4 - Initial Authentication Prototype

An initial authentication workflow was implemented using an in-memory repository.

The prototype contained demonstration accounts for:

```
member
admin
```

The prototype was used to verify:

* Login request handling
* Successful authentication
* Invalid credential handling
* Account information loading
* MEMBER and ADMIN roles
* Role-based home-page selection
* Logout behavior

This implementation was temporary and was used only to confirm the basic workflow before adding a real database.

## Step 5 - Browser User Interface

A browser-based user interface was created using HTML, CSS and JavaScript.

The first interface included:

* Username field
* Password field
* Login button
* Loading state
* Error-message area
* Successful-login confirmation
* Member home page
* Administrator dashboard
* Logout button

The frontend communicated with the backend authentication REST endpoint.

The interface was designed to remain simple, readable and responsive.

## Step 6 - Maven Build Configuration

The Maven `pom.xml` file was updated with the required dependencies:

* Spring Boot Web
* Spring Boot Validation
* Spring Boot Actuator
* Spring Data JPA
* PostgreSQL Driver
* Spring Security Crypto
* Spring Boot Test

The project was compiled and tested using:

```powershell
mvn clean install
```

Maven also created the executable Spring Boot JAR file.

## Step 7 - PostgreSQL Database Creation

PostgreSQL 18 and pgAdmin were used for database management.

A database named:

```
membernet
```

was created with the `postgres` user.

The local connection used:

```
Host: localhost
Port: 5432
Database: membernet
Username: postgres
```

The PostgreSQL connection was added to the Spring Boot configuration.

Database credentials were later moved to the `DB_PASSWORD` environment variable to prevent passwords from being committed to GitHub.

## Step 8 - Spring Data JPA Integration

Spring Data JPA and the PostgreSQL driver were added to the project.

The following persistence components were introduced:

```
UserAccountEntity
SpringDataUserAccountRepository
PostgresUserAccountRepository
```

`UserAccountEntity` mapped account information to PostgreSQL.

`SpringDataUserAccountRepository` provided database queries.

`PostgresUserAccountRepository` implemented the existing domain repository interface.

This design allowed the service layer to continue using `UserAccountRepository` without depending directly on Spring Data JPA.

## Step 9 - Replacement of In-Memory Storage

The temporary `InMemoryUserAccountRepository` was removed.

The final authentication workflow reads users from PostgreSQL.

The main database tables are:

```text
user_accounts
user_account_roles
```

The tables store:

* Username
* Display name
* Member ID
* Password hash
* MEMBER and ADMIN roles

The database rows and role assignments were verified using pgAdmin.

## Step 10 - Password Security

The initial prototype compared plain-text demonstration passwords.

This was replaced with BCrypt password hashing.

A `PasswordConfig` class was created to provide:

```
BCryptPasswordEncoder
```

When demonstration accounts are created, their passwords are hashed before being stored in PostgreSQL.

During login, BCrypt compares the submitted password with the stored hash.

This prevents the original password from being directly visible in the database.

## Step 11 - Database Initialization

A `DatabaseInitializer` was added.

The initializer creates demonstration accounts only when they do not already exist.

The accounts include:

* A MEMBER account
* An ADMIN account
* Additional training accounts where required

The ADMIN account contains both:

```
ADMIN
MEMBER
```

The initializer uses BCrypt before saving passwords.

An important observation was that changing an account in `DatabaseInitializer` does not update an existing database row. Existing rows were updated through PostgreSQL when display names or member IDs needed to change.

## Step 12 - Role-Based Home Pages

The authentication service loads the roles assigned to the authenticated account.

The role-based behavior is:

```
MEMBER -> Member home
ADMIN -> Administrator dashboard
```

The account display name, member ID and permissions are shown after successful login.

A successful login confirmation is also displayed.

## Step 13 - User Interface Improvements

Several interface improvements were completed.

```
Login
Logging in...
```

The temporary training-account information was removed from the visible login page.

Member IDs were changed to numeric-looking values.

Display names were updated in PostgreSQL.

The final interface clearly separates the Login page, Member home and Administrator dashboard.

## Step 14 - Error Handling

Error handling was implemented through:

```
InvalidCredentialsException
ApiErrorHandler
```

The application provides meaningful errors for invalid credentials and request-validation problems.

Unknown usernames and incorrect passwords produce a generic authentication error.

This prevents the response from revealing whether a specific account exists.

## Step 15 - Health Monitoring

Spring Boot Actuator was added.

The health endpoint is:

```
http://localhost:8080/actuator/health
```

A healthy application returns:

```json
{
  "status": "UP"
}
```

The endpoint supports manual verification, Docker operations and future Kubernetes health probes.

## Step 16 - Automated Testing

Automated controller tests were implemented using:

* Spring Boot Test
* JUnit
* Mockito

The tests verify successful authentication and error behavior.

The final verified Maven result was:

```
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

The build also confirmed:

* PostgreSQL 18.4 connection
* JPA initialization
* Detection of one JPA repository
* Creation of the executable JAR

## Step 17 - Manual Testing

The following cases were tested manually:

* Valid MEMBER login
* Valid ADMIN login
* Incorrect password
* Unknown username
* Empty input fields
* Account information loading
* Permission display
* Member home page
* Administrator dashboard
* Successful-login confirmation
* Logout
* Health endpoint
* PostgreSQL persistence

The database tables and roles were also checked through pgAdmin.

## Step 18 - Dockerfile Implementation

A Dockerfile was created for the Spring Boot application.

The Dockerfile:

* Uses an Eclipse Temurin Java 21 runtime image
* Copies the Maven-generated JAR
* Exposes port `8080`
* Runs as non-root user `1001`
* Starts the application with `java -jar`

The application image was built successfully.

## Step 19 - Docker Compose Integration

Docker Compose was configured with two services:

```
membernet
postgres
```

The `membernet` service runs the Spring Boot application.

The `postgres` service runs PostgreSQL 18.

MemberNet connects to PostgreSQL through:

```
jdbc:postgresql://postgres:5432/membernet
```

The database password is passed to both services through:

```
DB_PASSWORD
```

The PostgreSQL service includes a health check.

MemberNet waits until PostgreSQL becomes healthy before starting.

## Step 20 - PostgreSQL 18 Docker Volume Problem

During the first Docker test, PostgreSQL failed to start because PostgreSQL 18 uses a different recommended volume layout.

The initial mount was:

```text
/var/lib/postgresql/data
```

It was changed to:

```text
/var/lib/postgresql
```

A new PostgreSQL 18-specific volume was used.

After the change:

* PostgreSQL became healthy
* MemberNet connected successfully
* Hibernate created the tables
* Demonstration accounts were inserted
* Tomcat started on port `8080`

The application was then successfully tested through Docker.

## Step 21 - Environment Variables and Secret Protection

Database passwords were removed from committed configuration.

The application uses:

```
DB_PASSWORD
```

The variable is set locally in PowerShell:

```powershell
$env:DB_PASSWORD=''
```

The actual password is not included in the repository.

The `.gitignore` file excludes Maven build output, environment files, logs and IDE-specific files.

## Step 22 - Git and GitHub

Git was initialized in the project root.

The project was committed to the `main` branch and connected to a private GitHub repository.

The repository includes:

* Java source code
* Frontend files
* Maven configuration
* Docker configuration
* Automated tests
* README
* Sprint 1 documentation

The generated `target` directory is excluded from Git.

Changes are published using:

```powershell
git add .
git commit -m "Description of the change"
git push
```

## Step 23 - Documentation

The following documentation was created and updated:

```text
README.md
docs/requirements.md
docs/architecture.md
docs/technical-decisions.md
docs/development-log.md
docs/testing.md
docs/assumptions-risks.md
docs/ai-usage.md
```

The documentation records:

* Requirements
* Architecture
* Technical decisions
* Implementation notes
* Testing
* Risks
* AI usage
* Manual verification
* Lessons learned
* Future improvements

Older documentation references to the in-memory repository, plain-text passwords and Sprint 1 Kubernetes deployment were corrected.

## Step 24 - AI-Assisted Development

AI was used throughout the project for:

* Requirements analysis
* Technology comparison
* Architecture planning
* Initial code suggestions
* PostgreSQL integration
* BCrypt implementation
* Error diagnosis
* Maven output review
* Docker troubleshooting
* Test planning
* Documentation improvement

AI suggestions were reviewed before being accepted.

The trainee manually:

* Executed commands
* Reviewed code
* Corrected syntax
* Verified database data
* Tested browser behavior
* Checked Maven results
* Tested Docker
* Updated GitHub
* Made the final technical decisions

## Step 25 - Sprint 1 Completion Review

The final review checks that:

* The complete authentication workflow works
* MEMBER and ADMIN behavior is correct
* PostgreSQL persistence works
* Passwords are stored as BCrypt hashes
* Automated tests pass
* Docker Compose starts successfully
* The health endpoint returns `UP`
* No private password is committed
* Documentation matches the implementation
* GitHub contains the latest files

## Final Result

MemberNet Sprint 1 provides a complete browser-based authentication workflow using Java, Spring Boot, Maven, PostgreSQL, HTML, CSS, JavaScript and Docker.

The project demonstrates AI-assisted software development while keeping human review, testing, decisions and documentation visible.
