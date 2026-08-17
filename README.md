# MemberNet Sprint 1

MemberNet is a browser-based membership management application developed as part of the MemberNet AI Developer Training Project.

Sprint 1 demonstrates a complete authentication workflow using Java, Spring Boot, PostgreSQL and Docker.

## Features

* Browser-based login page
* Username and password authentication
* PostgreSQL account persistence
* BCrypt password hashing
* MEMBER and ADMIN roles
* Role-based home pages
* Account information display
* Meaningful validation and login errors
* Successful login confirmation
* Logout functionality
* Automated controller tests
* Spring Boot Actuator health endpoint
* Docker and Docker Compose support
* Environment-based database configuration
* Layered software architecture

## Technologies

* Java 21
* Spring Boot 3
* Maven
* Spring Data JPA
* PostgreSQL 18
* BCrypt
* HTML
* CSS
* JavaScript
* Docker
* Docker Compose
* Spring Boot Actuator
* JUnit
* Mockito
* Git and GitHub

## Architecture

MemberNet follows a layered software architecture:

```
Browser Interface
       |
       v
AuthenticationController
       |
       v
AuthenticationService
       |
       v
UserAccountRepository
       |
       v
PostgreSQL
```

The main layers are:

* **Presentation layer:** HTML, CSS and JavaScript
* **Controller layer:** receives REST requests
* **Service layer:** performs authentication and role-based decisions
* **Repository layer:** communicates with PostgreSQL
* **Database layer:** stores accounts, password hashes and roles

## User Roles

### MEMBER

A member can authenticate and access the Member home page.

### ADMIN

An administrator can authenticate and access the Administrator dashboard.

ADMIN accounts contain both `ADMIN` and `MEMBER` permissions.

## Project Structure

```
membernet-sprint1/
├── Backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/membernet/
│   │   │   │   ├── auth/
│   │   │   │   ├── config/
│   │   │   │   ├── user/
│   │   │   │   └── MemberNetApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       │   ├── index.html
│   │   │       │   ├── styles.css
│   │   │       │   └── app.js
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── docs/
├── .gitignore
├── docker-compose.yml
└── README.md
```

## Prerequisites

For local development, install:

* Java 21
* Maven
* PostgreSQL 18
* Git
* A browser
* VS Code or another IDE

For container-based execution, also install:

* Docker Desktop
* Docker Compose

Check the installations:

```powershell
java -version
mvn -version
git --version
docker --version
docker compose version
```

# Running Locally

## 1. Create the PostgreSQL Database

Open pgAdmin and create a database with these values:

```
Database name: membernet
Owner: postgres
Host: localhost
Port: 5432
```

Alternatively, create it using SQL:

```sql
CREATE DATABASE membernet;
```

The application automatically creates the required tables through Spring Data JPA.

## 2. Configure the Database Password

The PostgreSQL password must not be stored directly in Git.

The application reads it from the `DB_PASSWORD` environment variable.

In PowerShell:

```powershell
$env:DB_PASSWORD=''
```

## 3. Open the Backend Directory

```powershell
cd Backend
```

## 4. Build and Test the Application

```powershell
mvn clean install
```

A successful build should end with:

```
BUILD SUCCESS
```

## 5. Start the Application

```powershell
mvn spring-boot:run
```

Keep the terminal open while the application is running.

## 6. Open MemberNet

Open the following URL:

http://localhost:8080

## 7. Check Application Health

Open:

http://localhost:8080/actuator/health

Expected response:

```json
{
  "status": "UP"
}
```

## 8. Stop the Application

Return to the terminal and press:

```
Ctrl + C
```

# Running with Docker Compose

Docker Compose starts two services:

* `membernet` - the Spring Boot application
* `postgres` - the PostgreSQL 18 database

## 1. Open the Repository Root

If the terminal is inside `Backend`, return to the root:

```powershell
cd ..
```

## 2. Build the JAR File

The Dockerfile copies the Maven-generated JAR file, so build it before starting Docker:

```powershell
cd Backend
mvn clean package
cd ..
```

## 3. Set the Docker Database Password

```powershell
$env:DB_PASSWORD=''
```
Use a strong test password and do not store it directly in `docker-compose.yml`.

## 4. Validate Docker Compose

```powershell
docker compose config
```

This command should complete without configuration errors.

## 5. Build and Start the Containers

```powershell
docker compose up --build
```

Wait until PostgreSQL becomes healthy and MemberNet displays a message similar to:

```
Tomcat started on port 8080
Started MemberNetApplication
```

## 6. Test the Docker Application

Open:

http://localhost:8080

Check the health endpoint:

http://localhost:8080/actuator/health

## 7. Check Container Status

Open another PowerShell terminal in the repository root:

```powershell
docker compose ps
```

The MemberNet container should be running and PostgreSQL should be healthy.

## 8. View Container Logs

Application logs:

```powershell
docker compose logs membernet
```

PostgreSQL logs:

```powershell
docker compose logs postgres
```

Follow logs continuously:

```powershell
docker compose logs -f
```

## 9. Stop Docker Compose

```powershell
docker compose down
```

This removes the containers and Docker network but preserves the PostgreSQL volume.

# Demonstration Accounts

The application creates demonstration accounts through `DatabaseInitializer` when they do not already exist.

Example MEMBER account:

```
Username: member
Password: member123
```

Example ADMIN account:

```
Username: admin
Password: admin123
```

These credentials are for training and testing only. They must not be used in production.

# API Example

## Login Request

Example endpoint:

```
POST /api/auth/login
```

Example JSON request:

```json
{
  "username": "member",
  "password": "member123"
}
```

Example successful response:

```json
{
  "message": "Login successful.",
  "username": "member",
  "displayName": "Member",
  "memberId": "1001",
  "roles": [
    "MEMBER"
  ],
  "homePage": "Member home"
}
```

The exact display name can depend on the account data stored in PostgreSQL.

# Testing

## Automated Tests

Run:

```powershell
cd Backend
$env:DB_PASSWORD='YOUR_POSTGRESQL_PASSWORD'
mvn clean test
```

Or build and test together:

```powershell
mvn clean install
```

The final verified result was:

```
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Manual Tests

The following cases should be verified:

* Valid MEMBER login
* Valid ADMIN login
* Incorrect password
* Unknown username
* Empty username
* Empty password
* Account information display
* Role and permission display
* Successful login confirmation
* Logout
* Actuator health endpoint
* Docker-based execution

# Database

The application uses two main tables:

## `user_accounts`

Stores:

* ID
* Username
* BCrypt password hash
* Display name
* Member ID

## `user_account_roles`

Stores the roles assigned to each account:

* `MEMBER`
* `ADMIN`

# Security

Implemented security measures:

* BCrypt password hashing
* Generic invalid-credentials errors
* Unique usernames and member IDs
* Environment-based database passwords
* Non-root user in the Docker container
* Private GitHub repository during development


# Monitoring

Spring Boot Actuator provides the health endpoint:

```
GET /actuator/health
```

# Kubernetes

Kubernetes deployment is planned for Sprint 2.

Sprint 1 is prepared for future Kubernetes integration because it includes:

* Docker container support
* Environment-variable configuration
* Port `8080`
* Actuator health endpoint
* Layered and stateless application design

# Documentation

Additional documentation is available in the `docs` directory:

```
docs/
├── ai-usage.md
├── architecture.md
├── assumptions-risks.md
├── development-log.md
├── requirements.md
├── technical-decisions.md
└── testing.md
```

# AI Usage

AI was used for:

* Requirements analysis
* Technology evaluation
* Architecture planning
* Code generation support
* PostgreSQL integration
* Debugging
* Code review
* Test planning
* Docker troubleshooting
* Documentation improvement

All important AI suggestions were manually reviewed and tested.

# Git Workflow

Check the current changes:

```powershell
git status
```

Stage changes:

```powershell
git add .
```

Create a commit:

```powershell
git commit -m "Finalize Sprint 1 implementation and documentation"
```

Push to GitHub:

```powershell
git push
```

Final verification:

```powershell
git status
```

Expected result:

```
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

# Current Status

Sprint 1 includes:

* Complete authentication workflow
* PostgreSQL persistence
* MEMBER and ADMIN authorization
* Automated tests
* Docker deployment
* Health monitoring
* GitHub repository
* Technical and AI-use documentation

