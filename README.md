# 🚀 Onboarding Platform

Plataforma de onboarding multi-tenant construída com **DDD + Hexagonal Architecture**.

## 🏗️ Arquitetura

- **Domain-Driven Design (DDD)**
- **Hexagonal Architecture (Ports & Adapters)**
- **Saga Pattern** para orquestração
- **Multi-Tenancy** com strategy SCHEMA
- **Event-Driven Architecture** com Kafka

## 🛠️ Stack Tecnológica

- ☕ Java 17
- 🍃 Spring Boot 3.2.2
- 🗄️ PostgreSQL 16 (Multi-Tenancy)
- 🔐 Keycloak 23
- 📨 Apache Kafka
- 🐳 Docker & Docker Compose

## 📦 Bounded Contexts

```
├── Onboarding  → Gerencia processo de onboarding
├── Company     → Gerencia empresas
├── Identity    → Integração com Keycloak
└── Shared      → Objetos compartilhados (VOs, Events)
```

## 🚀 Quick Start

### Pré-requisitos

- JDK 17+
- Docker & Docker Compose
- Maven 3.8+

### Instalação

```bash
# 1. Clonar repositório
git clone https://github.com/lsouzaf/copilot-demos-onboarding-platform-ddd.git
cd copilot-demos-onboarding-platform-ddd

# 2. Setup completo (Docker + Build)
make setup

# 3. Executar aplicação
make run
```

A aplicação estará disponível em `http://localhost:8082`

### Serviços

- **API**: http://localhost:8082
- **Keycloak**: http://localhost:8080 (admin/admin)
- **Kafka UI**: http://localhost:8081
- **PostgreSQL**: localhost:5432

## 📡 Testando o Fluxo

### Iniciar Onboarding

```bash
curl -X POST http://localhost:8082/api/onboarding \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Acme Corp",
    "adminName": "John Doe",
    "adminEmail": "john@acme.com"
  }'
```

**Resposta:**
```json
{
  "onboardingId": "123e4567-e89b-12d3-a456-426614174000",
  "tenantId": "tenant_abc123",
  "status": "INITIATED"
}
```

### Monitorar Saga

Acesse o Kafka UI em http://localhost:8081 e monitore os tópicos:

1. ✅ `onboarding.initiated`
2. ✅ `onboarding.schema.created`
3. ✅ `onboarding.company.created`
4. ✅ `onboarding.realm.created`
5. ✅ `onboarding.admin.created`
6. ✅ `onboarding.email.sent`
7. ✅ `onboarding.completed`

## 🔄 Fluxo da Saga

```
POST /onboarding
    ↓
Criar Schema
    ↓
Criar Company
    ↓
Criar Realm Keycloak
    ↓
Criar Admin User
    ↓
Enviar Email
    ↓
Onboarding Completo
```

**Compensações**: Em caso de falha, a Saga reverte as operações na ordem inversa.

## 📁 Estrutura do Projeto

```
src/main/java/com/platform/
├── shared/
│   └── domain/              # Domain primitives
├── onboarding/
│   ├── domain/              # Agregados e Entidades
│   ├── application/         # Casos de uso
│   └── adapters/            # REST e Persistence
├── company/
│   ├── domain/
│   ├── application/
│   └── adapters/
└── infrastructure/
    ├── multitenancy/        # Hibernate Multi-Tenancy
    ├── keycloak/            # Keycloak Integration
    ├── kafka/               # Kafka Config
    └── saga/                # Saga Orchestrator
```

## 🧪 Testes

```bash
# Executar todos os testes
make test

# Testes de integração
./mvnw verify
```

## 🐛 Troubleshooting

### Kafka não conecta
```bash
docker-compose restart kafka
sleep 10
```

### Erro de schema
```sql
-- Conectar ao PostgreSQL
docker exec -it platform-postgres psql -U postgres -d platform_master

-- Criar schema template
CREATE SCHEMA IF NOT EXISTS template_schema;
```

## 📝 Licença

Este projeto está sob a licença MIT.

---

⭐ Se este projeto te ajudou, considere dar uma estrela!
