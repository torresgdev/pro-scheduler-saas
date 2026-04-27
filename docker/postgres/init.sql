CREATE TABLE tenants (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(50) UNIQUE NOT NULL
);

-- Inserindo uma barbearia de teste
INSERT INTO tenants (id, name, slug)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Barbearia do Mentor', 'barbearia-mentor');