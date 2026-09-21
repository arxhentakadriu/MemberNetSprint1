CREATE TABLE payment_obligations (
    id UUID PRIMARY KEY,

    user_account_id UUID NOT NULL,
    association_id UUID NOT NULL,
    membership_id UUID,

    amount NUMERIC(12, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'EUR',

    payment_reference VARCHAR(100) NOT NULL,
    description VARCHAR(500),

    due_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    paid_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_user_account
        FOREIGN KEY (user_account_id)
        REFERENCES user_accounts (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_payment_association
        FOREIGN KEY (association_id)
        REFERENCES associations (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_payment_membership
        FOREIGN KEY (membership_id)
        REFERENCES memberships (id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_payment_reference
        UNIQUE (payment_reference),

    CONSTRAINT chk_payment_amount
        CHECK (amount > 0),

    CONSTRAINT chk_payment_currency
        CHECK (currency_code = UPPER(currency_code)),

    CONSTRAINT chk_payment_status
        CHECK (
            status IN (
                'OPEN',
                'PAID',
                'OVERDUE',
                'CANCELLED'
            )
        ),

    CONSTRAINT chk_payment_paid_at
        CHECK (
            status <> 'PAID'
            OR paid_at IS NOT NULL
        )
);

CREATE TABLE payment_status_history (
    id UUID PRIMARY KEY,

    payment_obligation_id UUID NOT NULL,

    previous_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,

    changed_at TIMESTAMP WITH TIME ZONE NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_history_obligation
        FOREIGN KEY (payment_obligation_id)
        REFERENCES payment_obligations (id)
        ON DELETE CASCADE,

    CONSTRAINT chk_payment_history_previous_status
        CHECK (
            previous_status IS NULL
            OR previous_status IN (
                'OPEN',
                'PAID',
                'OVERDUE',
                'CANCELLED'
            )
        ),

    CONSTRAINT chk_payment_history_new_status
        CHECK (
            new_status IN (
                'OPEN',
                'PAID',
                'OVERDUE',
                'CANCELLED'
            )
        )
);

CREATE INDEX idx_payment_user_account
    ON payment_obligations (user_account_id);

CREATE INDEX idx_payment_association
    ON payment_obligations (association_id);

CREATE INDEX idx_payment_membership
    ON payment_obligations (membership_id);

CREATE INDEX idx_payment_status
    ON payment_obligations (status);

CREATE INDEX idx_payment_due_date
    ON payment_obligations (due_date);

CREATE INDEX idx_payment_history_obligation
    ON payment_status_history (payment_obligation_id);