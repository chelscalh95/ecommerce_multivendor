#!/bin/bash

# E-Commerce Multi-Vendor Platform - Development Runner Script
# This script provides various options to run the project with or without Docker

set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_menu() {
    echo -e "${BLUE}=====================================${NC}"
    echo -e "${GREEN}E-Commerce Multi-Vendor Dev Runner${NC}"
    echo -e "${BLUE}=====================================${NC}"
    echo "1) Run WITHOUT Docker (Local Development)"
    echo "2) Run WITH Docker (Full Stack)"
    echo "3) Run Backend Only (Spring Boot)"
    echo "4) Run Frontend Only (React)"
    echo "5) Run Docker Development Mode"
    echo "6) Stop All Services"
    echo "7) Clean and Rebuild"
    echo "8) View Logs"
    echo "9) Exit"
    echo -e "${BLUE}=====================================${NC}"
}

check_requirements() {
    echo -e "${YELLOW}Checking requirements...${NC}"
    
    if ! command -v java &> /dev/null; then
        echo -e "${RED}Java is not installed. Please install Java 17 or higher.${NC}"
        exit 1
    fi
    
    if ! command -v node &> /dev/null; then
        echo -e "${RED}Node.js is not installed. Please install Node.js 14 or higher.${NC}"
        exit 1
    fi
    
    if ! command -v mysql &> /dev/null; then
        echo -e "${YELLOW}MySQL client not found. Make sure MySQL server is running on localhost:3306${NC}"
    fi
    
    echo -e "${GREEN}✓ Basic requirements checked${NC}"
}

run_without_docker() {
    echo -e "${GREEN}Starting services WITHOUT Docker...${NC}"
    
    # Check if MySQL is running
    echo -e "${YELLOW}Checking MySQL connection...${NC}"
    if ! mysql -h localhost -P 3306 -u root -p -e "SELECT 1" &> /dev/null; then
        echo -e "${RED}MySQL is not running or accessible. Please start MySQL on localhost:3306${NC}"
        echo -e "${YELLOW}You can start MySQL with: brew services start mysql (on macOS) or sudo service mysql start (on Linux)${NC}"
        return 1
    fi
    
    # Start backend in background
    echo -e "${GREEN}Starting Spring Boot backend...${NC}"
    cd "backend-spring boot"
    # Using system Maven directly as mvnw wrapper files are missing
    mvn spring-boot:run &
    BACKEND_PID=$!
    cd ..
    
    # Wait for backend to start
    echo -e "${YELLOW}Waiting for backend to start...${NC}"
    sleep 10
    
    # Start frontend
    echo -e "${GREEN}Starting React frontend...${NC}"
    cd "fontend-react"
    if [ ! -d "node_modules" ]; then
        echo -e "${YELLOW}Installing frontend dependencies...${NC}"
        npm install
    fi
    npm start &
    FRONTEND_PID=$!
    cd ..
    
    echo -e "${GREEN}✓ Services started successfully!${NC}"
    echo -e "${BLUE}Backend running on: http://localhost:5454${NC}"
    echo -e "${BLUE}Frontend running on: http://localhost:3000${NC}"
    echo -e "${BLUE}API Documentation: http://localhost:5454/swagger-ui.html${NC}"
    echo -e "${YELLOW}PIDs - Backend: $BACKEND_PID, Frontend: $FRONTEND_PID${NC}"
    
    # Save PIDs for later
    echo $BACKEND_PID > .backend.pid
    echo $FRONTEND_PID > .frontend.pid
}

run_with_docker() {
    echo -e "${GREEN}Starting services WITH Docker...${NC}"
    
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null && ! command -v docker compose &> /dev/null; then
        echo -e "${RED}docker-compose is not installed. Please install docker-compose first.${NC}"
        exit 1
    fi
    
    echo -e "${YELLOW}Building and starting Docker containers...${NC}"
    if command -v docker-compose &> /dev/null; then
        docker-compose up -d --build
    else
        docker compose up -d --build
    fi
    
    echo -e "${GREEN}✓ Docker containers started successfully!${NC}"
    echo -e "${BLUE}Frontend: http://localhost:3000${NC}"
    echo -e "${BLUE}Backend API: http://localhost:5454${NC}"
    echo -e "${BLUE}API Documentation: http://localhost:5454/swagger-ui.html${NC}"
    echo -e "${YELLOW}View logs with: docker-compose logs -f${NC}"
}

run_backend_only() {
    echo -e "${GREEN}Starting Spring Boot backend only...${NC}"
    
    cd "backend-spring boot"
    # Using system Maven directly as mvnw wrapper files are missing
    mvn spring-boot:run
}

run_frontend_only() {
    echo -e "${GREEN}Starting React frontend only...${NC}"
    
    cd "fontend-react"
    if [ ! -d "node_modules" ]; then
        echo -e "${YELLOW}Installing dependencies...${NC}"
        npm install
    fi
    npm start
}

run_docker_dev() {
    echo -e "${GREEN}Starting Docker in development mode...${NC}"
    
    if [ -f "docker-compose.dev.yml" ]; then
        if command -v docker-compose &> /dev/null; then
            docker-compose -f docker-compose.dev.yml up --build
        else
            docker compose -f docker-compose.dev.yml up --build
        fi
    else
        echo -e "${YELLOW}docker-compose.dev.yml not found. Using default docker-compose.yml${NC}"
        if command -v docker-compose &> /dev/null; then
            docker-compose up --build
        else
            docker compose up --build
        fi
    fi
}

stop_all() {
    echo -e "${YELLOW}Stopping all services...${NC}"
    
    # Stop local services
    if [ -f ".backend.pid" ]; then
        kill $(cat .backend.pid) 2>/dev/null || true
        rm .backend.pid
    fi
    
    if [ -f ".frontend.pid" ]; then
        kill $(cat .frontend.pid) 2>/dev/null || true
        rm .frontend.pid
    fi
    
    # Stop Docker services
    if command -v docker-compose &> /dev/null; then
        docker-compose down 2>/dev/null || true
    else
        docker compose down 2>/dev/null || true
    fi
    
    # Kill any remaining Java or Node processes
    pkill -f "spring-boot:run" 2>/dev/null || true
    pkill -f "react-scripts start" 2>/dev/null || true
    
    echo -e "${GREEN}✓ All services stopped${NC}"
}

clean_rebuild() {
    echo -e "${YELLOW}Cleaning and rebuilding project...${NC}"
    
    # Clean backend
    echo -e "${BLUE}Cleaning backend...${NC}"
    cd "backend-spring boot"
    if [ -f "mvnw" ]; then
        ./mvnw clean
    else
        mvn clean
    fi
    cd ..
    
    # Clean frontend
    echo -e "${BLUE}Cleaning frontend...${NC}"
    cd "fontend-react"
    rm -rf node_modules
    rm -f package-lock.json
    npm install
    cd ..
    
    # Clean Docker
    echo -e "${BLUE}Cleaning Docker...${NC}"
    if command -v docker-compose &> /dev/null; then
        docker-compose down -v --remove-orphans 2>/dev/null || true
    else
        docker compose down -v --remove-orphans 2>/dev/null || true
    fi
    docker system prune -f
    
    echo -e "${GREEN}✓ Project cleaned and rebuilt${NC}"
}

view_logs() {
    echo -e "${BLUE}Select logs to view:${NC}"
    echo "1) Docker Compose logs"
    echo "2) Backend logs (if running locally)"
    echo "3) Frontend logs (if running locally)"
    echo "4) Back to main menu"
    
    read -p "Enter choice: " log_choice
    
    case $log_choice in
        1)
            if command -v docker-compose &> /dev/null; then
                docker-compose logs -f
            else
                docker compose logs -f
            fi
            ;;
        2)
            echo -e "${YELLOW}Backend logs are shown in the terminal where it's running${NC}"
            ;;
        3)
            echo -e "${YELLOW}Frontend logs are shown in the terminal where it's running${NC}"
            ;;
        *)
            return
            ;;
    esac
}

# Main script
check_requirements

while true; do
    print_menu
    read -p "Enter your choice (1-9): " choice
    
    case $choice in
        1)
            run_without_docker
            ;;
        2)
            run_with_docker
            ;;
        3)
            run_backend_only
            ;;
        4)
            run_frontend_only
            ;;
        5)
            run_docker_dev
            ;;
        6)
            stop_all
            ;;
        7)
            clean_rebuild
            ;;
        8)
            view_logs
            ;;
        9)
            echo -e "${GREEN}Goodbye!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}Invalid choice. Please try again.${NC}"
            ;;
    esac
    
    if [ "$choice" != "9" ]; then
        echo -e "\n${YELLOW}Press Enter to continue...${NC}"
        read
    fi
done