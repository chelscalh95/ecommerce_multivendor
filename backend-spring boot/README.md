# E-Commerce Multi-Vendor Backend

A comprehensive Spring Boot backend application for a multi-vendor e-commerce platform with AI integration, supporting Admin, Seller, and Customer roles.

## Quick Start

### Prerequisites
- Java 17 or later
- Maven 3.6+
- MySQL 8.0+
- Docker (optional)

### Running with Docker (Recommended)

1. **Clone and navigate to backend directory**
   ```bash
   cd "backend-spring boot"
   ```

2. **Set environment variables** (optional - create `.env` file)
   ```bash
   # Payment Gateway
   RAZORPAY_KEY=your_razorpay_key
   RAZORPAY_SECRET=your_razorpay_secret
   STRIPE_KEY=your_stripe_key
   
   # Email Service
   MAIL_USERNAME=your_email@gmail.com
   MAIL_PASSWORD=your_app_password
   
   # AI Service
   GEMINI_API_KEY=your_gemini_api_key
   ```

3. **Start with Docker Compose**
   ```bash
   docker-compose up -d
   ```

The application will be available at `http://localhost:5454`

### Running Locally

1. **Setup MySQL Database**
   ```sql
   CREATE DATABASE ecommerce_multi_vendor;
   ```

2. **Update application.properties**
   ```properties
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## Features

### 🛡️ Security & Authentication
- JWT-based authentication
- Role-based access control (Admin, Seller, Customer)
- Rate limiting (10 req/min for auth, 100 req/min for others)
- Password encryption with BCrypt
- Email verification system

### 🛒 E-Commerce Core
- Multi-vendor product management
- Shopping cart and wishlist
- Order processing and tracking
- Payment integration (Razorpay, Stripe)
- Review and rating system

### 🤖 AI Integration
- AI-powered chatbot
- Smart product recommendations
- Intelligent search functionality
- Product details assistance

### 👥 Multi-User Support
- **Admin**: User management, seller verification, analytics
- **Seller**: Product management, order fulfillment, revenue tracking
- **Customer**: Shopping, orders, reviews, wishlist

## API Documentation

Access interactive API documentation at:
- **Swagger UI**: `http://localhost:5454/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:5454/api-docs`

## Key Endpoints

### Authentication
- `POST /auth/signup` - User registration
- `POST /auth/signin` - User login
- `POST /auth/send-otp` - Send verification OTP

### Products
- `GET /api/products` - List products
- `GET /api/products/{id}` - Get product details
- `GET /api/products/search` - Search products

### Orders
- `POST /api/orders` - Create order
- `GET /api/orders/user` - User orders
- `PUT /api/orders/{id}/status` - Update order status

### Admin
- `GET /api/admin/sellers` - Manage sellers
- `GET /api/admin/users` - Manage users
- `POST /api/admin/coupons` - Create coupons

## Database Schema

The application uses a comprehensive database schema with 20+ entities including:

- **User Management**: User, VerificationCode, PasswordResetToken
- **Product Catalog**: Product, Category, Review
- **E-commerce**: Order, OrderItem, Cart, CartItem
- **Payments**: PaymentOrder, Transaction, Payouts
- **Seller Management**: Seller, SellerReport, BusinessDetails

## Configuration

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `DB_HOST` | Database host | Yes |
| `DB_PORT` | Database port | Yes |
| `DB_NAME` | Database name | Yes |
| `DB_USERNAME` | Database username | Yes |
| `DB_PASSWORD` | Database password | Yes |
| `RAZORPAY_KEY` | Razorpay API key | No |
| `RAZORPAY_SECRET` | Razorpay secret | No |
| `STRIPE_KEY` | Stripe API key | No |
| `MAIL_USERNAME` | Email username | No |
| `MAIL_PASSWORD` | Email password | No |
| `GEMINI_API_KEY` | Google Gemini API key | No |

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=5454

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_multi_vendor
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Security Configuration
jwt.secret=your_jwt_secret
jwt.expiration=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Development

### Building the Application

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Package
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

### Docker Commands

```bash
# Build image
docker build -t ecommerce-backend .

# Run container
docker run -p 5454:5454 ecommerce-backend

# View logs
docker logs ecommerce-backend

# Stop services
docker-compose down
```

## Testing

### Running Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=UserServiceTest

# Integration tests
mvn test -Dtest=*IntegrationTest
```

### Test Coverage

The application includes:
- Unit tests for services
- Integration tests for controllers
- Repository tests
- Security tests

## Monitoring & Health Checks

### Health Endpoints
- `GET /actuator/health` - Application health
- `GET /actuator/info` - Application info
- `GET /actuator/metrics` - Application metrics

### Logging

Application uses structured logging with different levels:
- `ERROR` - Error conditions
- `WARN` - Warning conditions
- `INFO` - Informational messages
- `DEBUG` - Debug information

## Production Deployment

### Performance Tuning

1. **JVM Configuration**
   ```bash
   export JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC"
   ```

2. **Database Connection Pool**
   ```properties
   spring.datasource.hikari.maximum-pool-size=20
   spring.datasource.hikari.minimum-idle=5
   ```

3. **Caching Configuration**
   ```properties
   spring.cache.type=redis
   spring.redis.host=redis-server
   ```

### Security Checklist

- [ ] Update default passwords
- [ ] Configure HTTPS
- [ ] Set up firewall rules
- [ ] Enable security headers
- [ ] Configure rate limiting
- [ ] Set up monitoring
- [ ] Regular security updates

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   ```
   Solution: Check MySQL service status and credentials
   ```

2. **JWT Token Invalid**
   ```
   Solution: Verify JWT secret and token expiration
   ```

3. **Payment Gateway Error**
   ```
   Solution: Check API keys and network connectivity
   ```

4. **Email Service Not Working**
   ```
   Solution: Verify SMTP settings and app passwords
   ```

### Debug Mode

Enable debug logging:
```bash
java -jar app.jar --logging.level.com.zosh=DEBUG
```

## Support

For detailed architecture information, see [BACKEND_ARCHITECTURE.md](./BACKEND_ARCHITECTURE.md)

### Documentation
- [API Documentation](http://localhost:5454/swagger-ui.html)
- [Architecture Guide](./BACKEND_ARCHITECTURE.md)
- [Database Schema](./docs/database-schema.md)

### Development Team
- Backend Development
- Database Administration
- DevOps & Deployment
- Security & Compliance

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Status**: ✅ Production Ready | 🚀 Fully Dockerized | 🔒 Security Hardened | 📚 Comprehensive Documentation