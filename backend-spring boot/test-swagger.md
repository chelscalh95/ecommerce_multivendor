# Swagger Documentation Test Guide

## Swagger UI URLs
After starting the application on port 5454, access these URLs:

- **Swagger UI**: http://localhost:5454/swagger-ui.html
- **API Docs JSON**: http://localhost:5454/api-docs
- **OpenAPI JSON**: http://localhost:5454/v3/api-docs

## Starting the Application
```bash
cd "backend-spring boot"
mvn spring-boot:run
```

## What to Test

### 1. Swagger UI Interface
- [ ] Swagger UI loads properly
- [ ] All controller tags are visible (Authentication, Products, Users, etc.)
- [ ] API endpoints are grouped by controllers
- [ ] Security authentication is available (JWT Bearer token)

### 2. API Documentation
- [ ] Each endpoint has proper documentation
- [ ] Request/response schemas are displayed
- [ ] Parameter descriptions are clear
- [ ] HTTP response codes are documented

### 3. Interactive Testing
- [ ] "Try it out" functionality works
- [ ] Authentication endpoints can be tested
- [ ] Bearer token can be added to secured endpoints
- [ ] Request/response examples are shown

## Controller Tags Added
1. **Authentication** - Login/signup operations
2. **Products** - Product catalog and search
3. **Users** - User profile management
4. **Admin** - Administrative operations
5. **Cart** - Shopping cart management
6. **Orders** - Order processing
7. **Sellers** - Seller management
8. **Reviews** - Product reviews
9. **Payments** - Payment processing
10. **AI Services** - AI-powered features

## Security Configuration
- JWT Bearer token authentication is configured
- Secured endpoints require "Bearer Authentication"
- Token format: "Bearer {your-jwt-token}"