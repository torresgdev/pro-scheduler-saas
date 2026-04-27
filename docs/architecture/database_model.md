# 1. Visão Geral

O sistema utiliza um banco de dados relacional (PostgreSQL) para garantir a integridade dos agendamentos e a consistência financeira. 
O modelo foi desenhado para suportar múltiplos prestadores de serviço (Multi-tenant) de forma isolada.

# 2. Diagrama de Entidade-Relacionamento (ERD)

# 3. Dicionário de Dados (Entidades Principais)

**A.** Tabela: tenants (O Estabelecimento)
No modelo SaaS, o "Tenant" é a empresa (ex: Barbearia do Centro). Tudo no sistema deve pertencer a um Tenant.

    id: UUID (Primary Key) - Uso de UUID é melhor para segurança em URLs.

    name: VARCHAR(100)

    slug: VARCHAR(50) - Ex: barbearia-do-centro (usado na URL do agendamento).

    created_at: TIMESTAMP


**B.** Tabela: clients (acesso ao sistema) 

    id: UUID (Primary Key)
    name: VARCHAR(100)
    phone: VARCHAR(30)
    isVerified: BOOLEAN(defautl:False) - muda pra ture depois da confirmacao do bot

**C.** Tabela: profissionals (prestadores do servico)

    id: UUID(Primary Key)
    tenant_id: UUID(foreing Key)
    name: VARCHAR(100)
    bio: TEXT

**D.** Tabela: services(oque é vendido)

    id: UUID(PRIMARY KEY)
    name: VARCHAR(100) - EX: "Corte de Cabelo"
    duration_minutes: INTEGER - Essencial para o calculo
    price: DECIMAL(10,2)

**E**. Tabela: appointments (O coração do sistema)

    id: UUID (Primary Key)
    tentant_id: UUID(FOREING KEY)
    professional_id: UUID(FFOREING KEY)
    service_id: UUID(FOREING KEY)
    client_name: VARCHAR(100)
    client_phone: VARCHAR(30)
    start_time: TIMESTAMP - data e hora de inicio
    end_time : TIMESTAMP - calculado via codigod:
    start_time+service.duration
    status: VARCHAR(20) - ex 'SCHEDULED', 'CANCELED', 'COMPLETED'
    
    
    


