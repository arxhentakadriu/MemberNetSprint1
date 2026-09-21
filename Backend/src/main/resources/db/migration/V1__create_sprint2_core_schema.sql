-- MemberNet Sprint 2 core database schema

CREATE TABLE user_accounts (
    id UUID PRIMARY KEY,
    login_email VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    account_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    language_code VARCHAR(10) NOT NULL DEFAULT 'en',
    time_zone VARCHAR(100) NOT NULL DEFAULT 'Europe/Helsinki',
    event_view_preference VARCHAR(20) NOT NULL DEFAULT 'LIST',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_user_accounts_login_email UNIQUE (login_email),
    CONSTRAINT chk_user_account_status
        CHECK (account_status IN ('ACTIVE', 'INACTIVE', 'LOCKED')),
    CONSTRAINT chk_event_view_preference
        CHECK (event_view_preference IN ('LIST', 'CALENDAR'))
);

CREATE TABLE associations (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    short_name VARCHAR(50) NOT NULL,
    business_id VARCHAR(100),
    country_code VARCHAR(2) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    terms_accepted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_associations_short_name UNIQUE (short_name),
    CONSTRAINT uk_associations_business_id UNIQUE (business_id),
    CONSTRAINT chk_association_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);

CREATE TABLE memberships (
    id UUID PRIMARY KEY,
    user_account_id UUID NOT NULL,
    association_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    valid_from DATE NOT NULL,
    valid_until DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_membership_user_account
        FOREIGN KEY (user_account_id)
        REFERENCES user_accounts (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_membership_association
        FOREIGN KEY (association_id)
        REFERENCES associations (id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_membership_user_association
        UNIQUE (user_account_id, association_id),

    CONSTRAINT uk_membership_id_association
        UNIQUE (id, association_id),

    CONSTRAINT chk_membership_status
        CHECK (status IN ('PENDING', 'ACTIVE', 'INACTIVE', 'EXPIRED')),

    CONSTRAINT chk_membership_validity
        CHECK (valid_until IS NULL OR valid_until >= valid_from)
);

CREATE TABLE roles (
    id UUID PRIMARY KEY,
    association_id UUID NOT NULL,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_role_association
        FOREIGN KEY (association_id)
        REFERENCES associations (id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_role_association_code
        UNIQUE (association_id, code),

    CONSTRAINT uk_role_id_association
        UNIQUE (id, association_id)
);

CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_permission_code UNIQUE (code)
);

CREATE TABLE membership_roles (
    membership_id UUID NOT NULL,
    role_id UUID NOT NULL,
    association_id UUID NOT NULL,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (membership_id, role_id),

    CONSTRAINT fk_membership_role_membership
        FOREIGN KEY (membership_id, association_id)
        REFERENCES memberships (id, association_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_membership_role_role
        FOREIGN KEY (role_id, association_id)
        REFERENCES roles (id, association_id)
        ON DELETE CASCADE
);

CREATE TABLE role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (role_id, permission_id),

    CONSTRAINT fk_role_permission_role
        FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permission_permission
        FOREIGN KEY (permission_id)
        REFERENCES permissions (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_memberships_user_account
    ON memberships (user_account_id);

CREATE INDEX idx_memberships_association
    ON memberships (association_id);

CREATE INDEX idx_roles_association
    ON roles (association_id);

CREATE INDEX idx_membership_roles_association
    ON membership_roles (association_id);