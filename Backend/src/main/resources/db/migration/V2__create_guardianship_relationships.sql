CREATE TABLE guardianship_relationships (
    id UUID PRIMARY KEY,

    guardian_user_account_id UUID NOT NULL,
    child_user_account_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    valid_from DATE NOT NULL DEFAULT CURRENT_DATE,
    valid_until DATE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_guardianship_guardian
        FOREIGN KEY (guardian_user_account_id)
        REFERENCES user_accounts (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_guardianship_child
        FOREIGN KEY (child_user_account_id)
        REFERENCES user_accounts (id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_guardianship_guardian_child
        UNIQUE (guardian_user_account_id, child_user_account_id),

    CONSTRAINT chk_guardianship_different_users
        CHECK (guardian_user_account_id <> child_user_account_id),

    CONSTRAINT chk_guardianship_status
        CHECK (status IN (
            'PENDING',
            'ACTIVE',
            'REJECTED',
            'EXPIRED',
            'TERMINATED'
        )),

    CONSTRAINT chk_guardianship_validity
        CHECK (
            valid_until IS NULL
            OR valid_until >= valid_from
        )
);

CREATE INDEX idx_guardianship_guardian
    ON guardianship_relationships (guardian_user_account_id);

CREATE INDEX idx_guardianship_child
    ON guardianship_relationships (child_user_account_id);

CREATE INDEX idx_guardianship_status
    ON guardianship_relationships (status);