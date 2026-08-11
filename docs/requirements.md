## Functional Requirements

| Requirement                   | Implementation                            | Status    |
| ----------------------------- | ----------------------------------------- | --------- |
| Display a login page          | Browser-based HTML login page             | Completed |
| Enter credentials             | Username and password fields              | Completed |
| Authenticate the user         | Spring service with PostgreSQL and BCrypt | Completed |
| Load account information      | User data loaded from PostgreSQL          | Completed |
| Determine permissions         | MEMBER and ADMIN roles                    | Completed |
| Display appropriate home page | Role-based home page content              | Completed |
| Confirm successful login      | Success message displayed                 | Completed |
| Allow logout                  | Logout button clears the browser session  | Completed |

## Not Included in Sprint 1

User registration is not part of the Sprint 1 requirements.
Accounts must exist in PostgreSQL before authentication.
Kubernetes deployment is planned for Sprint 2.
