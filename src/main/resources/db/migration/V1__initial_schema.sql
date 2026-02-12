-- Create public schema tables
CREATE TABLE IF NOT EXISTS public.onboarding_requests (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL UNIQUE,
    company_name VARCHAR(100) NOT NULL,
    admin_name VARCHAR(100) NOT NULL,
    admin_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    failure_reason TEXT,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP
);

CREATE INDEX idx_onboarding_tenant_id ON public.onboarding_requests(tenant_id);
CREATE INDEX idx_onboarding_status ON public.onboarding_requests(status);
CREATE INDEX idx_onboarding_created_at ON public.onboarding_requests(created_at);

-- Create template schema
CREATE SCHEMA IF NOT EXISTS template_schema;

-- Companies table (template)
CREATE TABLE IF NOT EXISTS template_schema.companies (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_template_company_name ON template_schema.companies(name);
