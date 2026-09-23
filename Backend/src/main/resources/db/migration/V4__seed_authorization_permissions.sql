-- MemberNet Sprint 2 baseline authorization permissions

INSERT INTO permissions (
    id,
    code,
    name,
    description
)
VALUES
(
    '91893441-b94b-4c1b-bd54-b664c5dbf996',
    'PAYMENT_MANAGE',
    'Manage Payments',
    'Allows authorized members to create and update payment obligations.'
),
(
    'b6ee4c80-77db-4515-bb1a-28927a60a5a0',
    'MEMBERSHIP_MANAGE',
    'Manage Memberships',
    'Allows authorized members to create and manage association memberships.'
),
(
    '1b83121e-3c12-4cbd-95ef-a79eb8acfde2',
    'AUTHORIZATION_MANAGE',
    'Manage Authorization',
    'Allows authorized members to manage roles and permissions.'
)
ON CONFLICT (code)
DO UPDATE SET
    name = EXCLUDED.name,
    description = EXCLUDED.description;