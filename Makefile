.PHONY: help build test run docker-up docker-down clean setup

help:
	@echo "🚀 Onboarding Platform - Comandos Disponíveis"
	@echo ""
	@echo "  make setup        - Setup completo (Docker + Build)"
	@echo "  make build        - Compilar o projeto"
	@echo "  make test         - Executar testes"
	@echo "  make run          - Executar aplicação"
	@echo "  make docker-up    - Iniciar containers Docker"
	@echo "  make docker-down  - Parar containers Docker"
	@echo "  make clean        - Limpar build"

setup: docker-up build
	@echo "✅ Setup completo!"

build:
	@echo "🔨 Compilando projeto..."
	./mvnw clean install -DskipTests

test:
	@echo "🧪 Executando testes..."
	./mvnw test

run:
	@echo "🚀 Iniciando aplicação..."
	./mvnw spring-boot:run

docker-up:
	@echo "🐳 Iniciando containers..."
	docker-compose up -d
	@echo "⏳ Aguardando serviços (30s)..."
	@sleep 30
	@echo "✅ Containers prontos!"

docker-down:
	@echo "🛑 Parando containers..."
	docker-compose down -v

clean:
	@echo "🧹 Limpando build..."
	./mvnw clean
	docker-compose down -v
