# Default docker compose file
COMPOSE = docker compose -f docker/docker-compose.yml

# Bring up stack
up:
	$(COMPOSE) up -d --build

# Stop and remove containers/networks
down:
	$(COMPOSE) down

# Stop without removing
stop:
	$(COMPOSE) stop

# Restart without rebuilding
restart:
	$(COMPOSE) restart

# Rebuild everything from scratch
rebuild:
	$(COMPOSE) build --no-cache
	$(COMPOSE) up -d --force-recreate

# Rebuild only shop-web
rebuild-web:
	$(COMPOSE) build --no-cache shop-web
	$(COMPOSE) up -d --no-deps --force-recreate shop-web

# Rebuild only shop-api
rebuild-api:
	$(COMPOSE) build --no-cache shop-api
	$(COMPOSE) up -d --no-deps --force-recreate shop-api

# Tail logs for all services
logs:
	$(COMPOSE) logs -f

# Tail logs for a specific service (ex: make logs SERVICE=shop-web)
logs-service:
	$(COMPOSE) logs -f $(SERVICE)
