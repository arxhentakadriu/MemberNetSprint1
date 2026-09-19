# MemberNet Sprint 2 – Architecture Design

## 1. Architecture Style

Sprint 2 will continue using a layered architecture:

1. Controller Layer
2. Service Layer
3. Repository Layer
4. Domain and Persistence Layer
5. PostgreSQL Database

The browser communicates with the backend through REST APIs using JSON.

## 2. Main Components

### Controller Layer

Receives HTTP requests, validates request data and returns appropriate
HTTP responses.

### Service Layer

Contains the application behavior, business rules and authorization
decisions.

### Repository Layer

Provides controlled access to persistent information without exposing
database details to the service layer.

### Persistence Layer

Maps MemberNet entities and relationships to PostgreSQL tables.

## 3. Core Domain Model

The initial Sprint 2 implementation contains:

- UserAccount
- Group
- Membership
- Role
- Permission
- MembershipRole
- RolePermission

Guardianship and PaymentObligation will be added after the core
membership model is operational.

## 4. Entity Relationships

- One UserAccount may have multiple Memberships.
- One Group may contain multiple Memberships.
- Each Membership belongs to one UserAccount and one Group.
- One Group may define multiple Roles.
- One Membership may have multiple Roles.
- One Role may contain multiple Permissions.
- A Role assigned to a Membership must belong to the same Group.

## 5. Authentication

Spring Security session-based authentication will be used.

After successful login:

- The authenticated User Account is loaded.
- The user's available Memberships are loaded.
- An active Group is selected.
- Roles and Permissions are evaluated within that Group.
- Global Logout invalidates the session and security context.

## 6. Authorization

Authorization will be enforced by the backend.

Every protected operation must verify:

1. The authenticated User Account.
2. The active Group.
3. The User Account's Membership in that Group.
4. The required Role or Permission.

Frontend visibility is not considered sufficient authorization.

## 7. Persistence

PostgreSQL will remain the relational database.

UUID identifiers will be used for new Sprint 2 entities. Foreign keys,
unique constraints and validation rules will protect data integrity.

Database changes will be managed using Flyway migrations.

## 8. Initial Package Structure

```text
com.membernet
├── authentication
├── user
├── group
├── membership
├── authorization
├── common
└── config
```
