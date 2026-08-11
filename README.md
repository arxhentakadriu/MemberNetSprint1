# MemberNet Sprint 1

MemberNet is a browser-based membership management application.
Sprint 1 demonstrates a complete authentication workflow using
Java, Spring Boot and PostgreSQL.

## Implemented Features

- Login page
- Username and password authentication
- PostgreSQL user storage
- BCrypt password hashing
- Member and administrator roles
- Role-based home pages
- Account information display
- Meaningful login errors
- Successful login confirmation
- Logout functionality
- Health monitoring endpoint
- Docker support

## Technologies

- Java 21
- Spring Boot
- Maven
- PostgreSQL 18
- HTML
- CSS
- JavaScript
- Docker
- Spring Boot

## User Roles

### MEMBER

A member can authenticate and access the Member home page.

### ADMIN

An administrator can authenticate and access the Administrator
dashboard.

## Running the Application

1. Create a PostgreSQL database named `membernet`.
2. Configure the database connection.
3. Open a terminal in the `Backend` directory.
4. Build the project:

````bash
mvn clean install

Open [http://localhost:8080](http://localhost:8080).

## Verify and package

```powershell
cd Backend
mvn test
mvn package
docker build -t membernet:latest .
````

Or, after packaging, run `docker compose up --build` from the repository root.

Example login request:

```json
{ "username": "member", "password": "member123" }
```
