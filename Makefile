.PHONY: help install run run-backend run-frontend stop clean migrate restart

# Default target - show help
help:
	@echo "🏀 NBA Analytics Platform - Available Commands"
	@echo ""
	@echo "  make install          - Install all dependencies (backend + frontend)"
	@echo "  make run              - Run both backend and frontend concurrently"
	@echo "  make run-backend      - Run backend only (Spring Boot)"
	@echo "  make run-frontend     - Run frontend only (React + Vite)"
	@echo "  make stop             - Stop all running processes"
	@echo "  make migrate          - Run data migration from SQLite to PostgreSQL"
	@echo "  make clean            - Clean build artifacts"
	@echo "  make restart          - Stop and restart everything"
	@echo ""

# Install all dependencies
install:
	@echo "📦 Installing backend dependencies..."
	@cd backend && mvn clean install -DskipTests
	@echo ""
	@echo "📦 Installing frontend dependencies..."
	@cd frontend && npm install
	@echo ""
	@echo "✅ All dependencies installed!"

# Run both backend and frontend
run:
	@echo "🚀 Starting NBA Analytics Platform..."
	@echo ""
	@echo "Backend will run on: http://localhost:8080"
	@echo "Frontend will run on: http://localhost:3000"
	@echo ""
	@echo "Press Ctrl+C to stop both services"
	@echo ""
	@trap 'kill 0' EXIT; \
	(cd backend && export JAVA_HOME=$$(/usr/libexec/java_home -v 17) && mvn spring-boot:run) & \
	(cd frontend && npm run dev) & \
	wait

# Run backend only
run-backend:
	@echo "🔧 Starting backend on http://localhost:8080..."
	@cd backend && export JAVA_HOME=$$(/usr/libexec/java_home -v 17) && mvn spring-boot:run

# Run frontend only
run-frontend:
	@echo "⚛️  Starting frontend on http://localhost:3000..."
	@cd frontend && npm run dev

# Stop all processes
stop:
	@echo "🛑 Stopping all NBA Analytics processes..."
	@pkill -f "spring-boot:run" || true
	@pkill -f "vite" || true
	@echo "✅ All processes stopped"

# Run data migration
migrate:
	@echo "📊 Running data migration..."
	@echo "This will import NBA data from SQLite to PostgreSQL"
	@curl -X POST http://localhost:8080/api/data-engineering/migrate-initial \
		-H "Content-Type: application/json" | python3 -m json.tool
	@echo ""
	@echo "✅ Migration complete!"

# Clean build artifacts
clean:
	@echo "🧹 Cleaning build artifacts..."
	@cd backend && mvn clean
	@cd frontend && rm -rf node_modules dist
	@echo "✅ Clean complete!"

# Restart everything
restart: stop run

# Build frontend for production
build-frontend:
	@echo "📦 Building frontend for production..."
	@cd frontend && npm run build
	@echo "✅ Frontend built successfully!"

# Build backend
build-backend:
	@echo "📦 Building backend..."
	@cd backend && mvn clean package -DskipTests
	@echo "✅ Backend built successfully!"

# Build everything
build: build-backend build-frontend
	@echo "✅ All builds complete!"

# Quick start (install + run)
start: install run

# Check if services are running
status:
	@echo "🔍 Checking service status..."
	@echo ""
	@if pgrep -f "spring-boot:run" > /dev/null; then \
		echo "✅ Backend is running"; \
	else \
		echo "❌ Backend is not running"; \
	fi
	@if pgrep -f "vite" > /dev/null; then \
		echo "✅ Frontend is running"; \
	else \
		echo "❌ Frontend is not running"; \
	fi

# Run tests
test-backend:
	@echo "🧪 Running backend tests..."
	@cd backend && mvn test

# Logs
logs-backend:
	@tail -f backend/logs/*.log

# Database commands
db-start:
	@echo "🗄️  Starting PostgreSQL..."
	@brew services start postgresql || pg_ctl start

db-stop:
	@echo "🗄️  Stopping PostgreSQL..."
	@brew services stop postgresql || pg_ctl stop

db-reset:
	@echo "⚠️  Resetting database..."
	@psql -U postgres -d nba_analytics -c "TRUNCATE TABLE play_by_play, game, common_player_info, player, team, users CASCADE;"
	@echo "✅ Database reset complete!"
