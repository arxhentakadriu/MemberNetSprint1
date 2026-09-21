# Sprint 2 API Usage Guide

## 1. Base URL

Local application:

```text
http://localhost:8080
```

Kubernetes port-forward:

```text
http://localhost:8081
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## 2. Authentication

### Login

```http
POST /api/auth/login
Content-Type: application/json
```

Example request:

```json
{
  "loginEmail": "admin@example.com",
  "password": "LOCAL_TEST_PASSWORD"
}
```

Example response:

```json
{
  "message": "Login successful. Welcome, Admin User.",
  "userAccountId": "c0418846-f86e-4fa8-98b0-e564d0536901",
  "loginEmail": "admin@example.com",
  "displayName": "Admin User",
  "accountStatus": "ACTIVE",
  "homePage": "MemberNet home"
}
```

### Current Session

```http
GET /api/auth/session
```

Returns the authenticated user when a valid HTTP session exists.

### Logout

```http
POST /api/auth/logout
```

Logout invalidates the current HTTP session.

## 3. Session Testing with PowerShell

Create the login body:

```powershell
$body = @{
    loginEmail = "admin@example.com"
    password   = "LOCAL_TEST_PASSWORD"
} | ConvertTo-Json
```

Login and store the session:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/api/auth/login" `
  -ContentType "application/json" `
  -Body $body `
  -SessionVariable membernetSession
```

Use the session:

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:8080/api/associations" `
  -WebSession $membernetSession
```

## 4. Associations

### List Associations

```http
GET /api/associations
```

### Get Association

```http
GET /api/associations/{id}
```

### Create Association

```http
POST /api/associations
Content-Type: application/json
```

Example:

```json
{
  "name": "Example Association",
  "shortName": "EXAMPLE",
  "businessId": "BUS-10001",
  "countryCode": "XK",
  "email": "association@example.com",
  "termsAccepted": true
}
```

## 5. Memberships

Membership endpoints use:

```text
/api/memberships
```

A membership connects:

- A user account
- An association
- A status
- A validity period

Use Swagger UI to inspect the exact request format generated from the Java request record.

## 6. Authorization

Authorization endpoints manage:

- Roles
- Permissions
- Role-permission assignments
- Membership-role assignments

Base path:

```text
/api/authorization
```

Role and permission identifiers must reference existing database records.

## 7. Guardianships

Base path:

```text
/api/guardianships
```

A guardianship request connects a guardian user account to a child user account.

The two identifiers must be different.

Supported statuses:

```text
PENDING
ACTIVE
REJECTED
EXPIRED
TERMINATED
```

## 8. Payments

Base path:

```text
/api/payments
```

Payment obligations contain:

- User account
- Association
- Optional membership
- Amount
- Currency
- Payment reference
- Description
- Due date
- Status

Supported statuses:

```text
OPEN
PAID
OVERDUE
CANCELLED
```

Payment amounts must be greater than zero.

## 9. Common HTTP Status Codes

| Status                      | Meaning                           |
| --------------------------- | --------------------------------- |
| `200 OK`                    | Request completed successfully    |
| `201 Created`               | Resource created successfully     |
| `400 Bad Request`           | Request validation failed         |
| `401 Unauthorized`          | Authentication is required        |
| `404 Not Found`             | Referenced resource was not found |
| `409 Conflict`              | Duplicate or conflicting data     |
| `500 Internal Server Error` | Unexpected server error           |

## 10. Error Response Format

API errors use a consistent JSON format:

```json
{
  "message": "Description of the error."
}
```

## 11. Important Note

Exact endpoint paths and request schemas should be verified through Swagger UI because the OpenAPI document is generated directly from the running application.
