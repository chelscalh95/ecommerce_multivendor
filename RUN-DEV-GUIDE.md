# Development Environment Setup Guide

## Quick Start

Run the development environment using the provided script:

```bash
./run-dev.sh
```

## Available Options

### 1. Run WITHOUT Docker (Local Development)
- Requires: Java 17+, Node.js 14+, MySQL running on localhost:3306
- Backend runs on port 5454
- Frontend runs on port 3000
- Hot-reload enabled for both services

### 2. Run WITH Docker (Full Stack)
- Requires: Docker and docker-compose installed
- Automatically sets up MySQL, backend, and frontend
- Production-like environment

### 3. Run Backend Only
- Starts only the Spring Boot backend
- Useful for API development and testing

### 4. Run Frontend Only
- Starts only the React frontend
- Useful for UI development

### 5. Run Docker Development Mode
- Uses docker-compose.dev.yml if available
- Optimized for development with volume mounts

### 6. Stop All Services
- Stops all running services (both local and Docker)
- Cleans up background processes

### 7. Clean and Rebuild
- Cleans Maven/npm caches
- Reinstalls dependencies
- Removes Docker volumes

### 8. View Logs
- Access logs for different services

## Manual Commands

### Without Docker:

**Backend:**
```bash
cd "backend-spring boot"
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd fontend-react
npm install
npm start
```

### With Docker:

**Start all services:**
```bash
docker-compose up -d
```

**View logs:**
```bash
docker-compose logs -f
```

**Stop all services:**
```bash
docker-compose down
```

## Access Points

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:5454
- **API Documentation**: http://localhost:5454/swagger-ui.html

## Prerequisites

### For Local Development:
- Java 17 or higher
- Node.js 14 or higher
- MySQL 8.0 or higher
- Maven (optional if using mvnw)

### For Docker Development:
- Docker Desktop
- docker-compose

## Environment Variables

The backend expects these environment variables (already configured in application.properties):
- Database: MySQL on localhost:3306
- Database name: ecommerce_multi_vendor
- Server port: 5454

## Troubleshooting

1. **MySQL Connection Error**: Ensure MySQL is running on localhost:3306
2. **Port Already in Use**: Stop any services using ports 3000 or 5454
3. **Docker Build Fails**: Run `docker system prune -a` to clean up
4. **Frontend Dependencies Error**: Delete node_modules and package-lock.json, then reinstall