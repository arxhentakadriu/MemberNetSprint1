# MemberNet Sprint 2 – Requirements Analysis

## 1. Objective

The objective of Sprint 2 is to extend MemberNet from a simple
authentication application into a foundation for managing users,
memberships, groups, roles and permissions.

Sprint 2 continues the work completed in Sprint 1, but introduces
a richer business model, persistent information, documented REST
interfaces and stronger security boundaries.

## 2. Defined Requirements

The following requirements are defined by the Sprint 2 material:

- A person is represented by one User Account.
- A User Account may have zero or multiple Memberships.
- Each Membership connects one User Account to one Group or Association.
- Roles are assigned through Memberships.
- Permissions are grouped through Roles.
- Roles must belong to the same Group as the related Membership.
- Guardianship connects two User Accounts.
- Authentication must complete before protected resources are accessed.
- Authorization must be checked for every protected operation.
- Global Logout must be available from every authenticated page.
- Application data must be stored persistently.
- REST APIs must use DTOs, validation and consistent HTTP status codes.
- Access between different Groups must be prevented.
- Passwords must never be stored as readable text.

## 3. Current Technical Assumptions

The current implementation assumes:

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Spring Security
- Session-based authentication
- UUID identifiers for Sprint 2 entities
- REST APIs using JSON
- HTML, CSS and JavaScript frontend
- Docker-based local deployment
- Kubernetes compatibility

These are implementation choices and may be reviewed if the project
requirements change.

## 4. Main Business Entities

Sprint 2 introduces the following main entities:

- User Account
- Group or Association
- Membership
- Role
- Permission
- Guardianship Relationship
- Payment Obligation

## 5. Important Change from Sprint 1

In Sprint 1, a Role was connected directly to a User Account.

In Sprint 2, Roles are assigned through Memberships. This allows the
same User Account to have different responsibilities in different
Groups.

Example:

- A user may be a Coach in Group A.
- The same user may be a Member in Group B.

## 6. Open Design Questions

The following items still require design decisions:

- Final database tables and constraints
- Final REST endpoint paths and payloads
- Final Role and Permission catalogue
- Membership status and validity rules
- Active Group storage and selection
- Guardianship approval workflow
- Payment-provider integration
- Final production deployment topology

## 7. Initial Implementation Scope

The first implementation phase will focus on:

1. User Accounts
2. Groups
3. Memberships
4. Roles and Permissions
5. Authentication and Global Logout
6. Active Group context
7. PostgreSQL persistence
8. REST APIs and validation
9. Authorization between Groups
10. Automated tests

Guardianship and Payments will be implemented in later phases after
the core membership and authorization model is working correctly.
