CREATE TABLE financial_transactions (
    id UUID NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    transaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type VARCHAR(25),
    payment_method VARCHAR(25),
    description TEXT,

    appointment_id UUID REFERENCES appointments(id),
    tenant_id UUID NOT NULL REFERENCES tenants(id),

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);