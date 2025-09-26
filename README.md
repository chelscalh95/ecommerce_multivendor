# E-Commerce Multi-Vendor Platform

A comprehensive multi-vendor e-commerce platform built with Spring Boot (Backend) and React.js (Frontend). This platform allows multiple vendors to sell their products while providing customers with a seamless shopping experience.

## 🚀 Features

### For Customers
- **User Authentication & Authorization** - Secure login/signup with JWT tokens
- **Product Browsing & Search** - Advanced filtering and search capabilities
- **Shopping Cart & Wishlist** - Add items to cart and save favorites
- **Order Management** - Track orders with real-time status updates
- **Payment Integration** - Support for Razorpay and Stripe payment gateways
- **Reviews & Ratings** - Rate and review products
- **AI Chatbot** - Intelligent customer support with Gemini AI
- **Address Management** - Multiple shipping addresses
- **Coupon System** - Apply discount coupons during checkout

### For Vendors/Sellers
- **Seller Dashboard** - Comprehensive analytics and insights
- **Product Management** - Add, edit, and manage product inventory
- **Order Processing** - Manage customer orders and fulfillment
- **Revenue Analytics** - Detailed sales reports and charts
- **Payout Management** - Track earnings and payment schedules
- **Transaction History** - Complete transaction tracking

### For Administrators
- **Admin Dashboard** - System-wide analytics and management
- **Seller Management** - Approve/reject seller applications
- **Coupon Management** - Create and manage discount coupons
- **Deal Management** - Featured deals and promotions
- **Category Management** - Organize products into categories
- **System Monitoring** - Monitor platform performance

## 🛠️ Technology Stack

### Backend (Spring Boot)
- **Java 17+** - Core programming language
- **Spring Boot 3.x** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL** - Primary database
- **JWT** - Token-based authentication
- **Swagger/OpenAPI** - API documentation
- **Maven** - Dependency management

### Frontend (React.js)
- **React 18** - Frontend framework
- **TypeScript** - Type-safe JavaScript
- **Redux Toolkit** - State management
- **Material-UI (MUI)** - UI component library
- **Axios** - HTTP client
- **React Router** - Client-side routing
- **Formik + Yup** - Form handling and validation
- **Tailwind CSS** - Utility-first CSS framework

### DevOps & Deployment
- **Docker** - Containerization
- **Docker Compose** - Multi-container deployment
- **Nginx** - Reverse proxy and static file serving
- **MySQL** - Database with Docker support

## 📁 Project Structure

```
ecommerce_multivendor/
├── backend-spring boot/          # Spring Boot backend
│   ├── src/main/java/com/ecom/   # Java source code
│   │   ├── config/               # Configuration classes
│   │   ├── controller/           # REST controllers
│   │   ├── model/                # Entity classes
│   │   ├── repository/           # Data repositories
│   │   ├── service/              # Business logic
│   │   ├── security/             # Security configurations
│   │   └── ai/                   # AI chatbot services
│   ├── src/main/resources/       # Application properties
│   ├── Dockerfile                # Backend Docker configuration
│   ├── docker-compose.yml        # Backend services
│   └── .env                      # Environment variables
├── fontend-react/                # React frontend
│   ├── src/                      # React source code
│   │   ├── admin/                # Admin panel components
│   │   ├── customer/             # Customer interface
│   │   ├── seller/               # Seller dashboard
│   │   ├── Redux Toolkit/        # State management
│   │   └── Config/               # API configuration
│   ├── public/                   # Static assets
│   ├── Dockerfile                # Frontend Docker configuration
│   ├── nginx.conf                # Nginx configuration
│   ├── docker-compose.yml        # Frontend services
│   └── .env                      # Environment variables
├── docker-compose.yml            # Full project orchestration
└── README.md                     # This file
```

## 🚀 Quick Start

### Prerequisites
- **Docker & Docker Compose** (Recommended)
- **Java 17+** (for local backend development)
- **Node.js 16+** (for local frontend development)
- **MySQL 8.0+** (if running locally)

### Option 1: Docker Deployment (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ecommerce_multivendor
   ```

2. **Start the entire application**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:5454
   - API Documentation: http://localhost:5454/swagger-ui.html

### Option 2: Local Development

#### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd backend-spring\ boot
   ```

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env file with your configurations
   ```

3. **Start MySQL database**
   ```bash
   docker-compose up mysql -d
   ```

4. **Run the Spring Boot application**
   ```bash
   ./mvnw spring-boot:run
   ```

#### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd fontend-react
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env file with your configurations
   ```

4. **Start the React application**
   ```bash
   npm start
   ```

## ⚙️ Configuration

### Backend Environment Variables

Create `.env` file in `backend-spring boot/` directory:

```env
# Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce_multi_vendor
DB_USERNAME=root
DB_PASSWORD=your_password

# Payment Gateway Configuration
RAZORPAY_KEY=your_razorpay_key
RAZORPAY_SECRET=your_razorpay_secret
STRIPE_KEY=your_stripe_key

# Email Configuration
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# AI Configuration
GEMINI_API_KEY=your_gemini_api_key

# JWT Configuration
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000
```

### Frontend Environment Variables

Create `.env` file in `fontend-react/` directory:

```env
# API Configuration
REACT_APP_API_URL=http://localhost:5454
REACT_APP_DOCKER=false

# Payment Configuration
REACT_APP_STRIPE_PUBLIC_KEY=your_stripe_public_key
REACT_APP_RAZORPAY_KEY=your_razorpay_key

# Feature Flags
REACT_APP_ENABLE_AI_CHATBOT=true
REACT_APP_ENABLE_WISHLIST=true
REACT_APP_ENABLE_REVIEWS=true
```

## 🐳 Docker Deployment

### Single Command Deployment

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes (careful - this deletes data)
docker-compose down -v
```

### Individual Service Management

```bash
# Start only database
docker-compose up mysql -d

# Start backend
docker-compose up backend -d

# Start frontend
docker-compose up frontend -d
```

## 📊 API Documentation

The backend provides comprehensive API documentation through Swagger UI:

- **Swagger UI**: http://localhost:5454/swagger-ui.html
- **API Docs**: http://localhost:5454/api-docs

### Key API Endpoints

```
Authentication:
POST /auth/signup - User registration
POST /auth/signin - User login

Products:
GET /api/products - Get all products
GET /api/products/{id} - Get product by ID
POST /api/admin/products - Create product (Admin)

Orders:
GET /api/orders/user - Get user orders
POST /api/orders - Create new order

Cart:
GET /api/cart/user - Get user cart
POST /api/cart/add - Add item to cart

Admin:
GET /api/admin/sellers - Get all sellers
PUT /api/admin/sellers/{id}/status - Update seller status
```

## 🏗️ Development

### Backend Development

```bash
# Run tests
./mvnw test

# Build JAR
./mvnw clean package

# Run with profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend Development

```bash
# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test
```

## 🔒 Security Features

- **JWT Authentication** - Secure token-based authentication
- **Role-based Access Control** - Different permissions for users, sellers, and admins
- **Input Validation** - Comprehensive validation on all inputs
- **CORS Configuration** - Proper CORS setup for API access
- **SQL Injection Prevention** - JPA with prepared statements
- **Rate Limiting** - API rate limiting to prevent abuse

## 🎯 Performance Optimizations

- **Lazy Loading** - React components and routes
- **Image Optimization** - Compressed images with caching
- **Database Indexing** - Optimized database queries
- **Nginx Caching** - Static asset caching
- **Connection Pooling** - Database connection optimization

## 🚀 Deployment

### Production Deployment

1. **Environment Setup**
   ```bash
   # Update environment variables for production
   NODE_ENV=production
   SPRING_PROFILES_ACTIVE=production
   ```

2. **SSL Configuration**
   ```bash
   # Add SSL certificates to nginx configuration
   # Update API URLs to use HTTPS
   ```

3. **Database Migration**
   ```bash
   # Run database migrations
   ./mvnw flyway:migrate
   ```

### Cloud Deployment Options

- **AWS**: ECS, RDS, CloudFront
- **Google Cloud**: Cloud Run, Cloud SQL, Cloud CDN
- **Azure**: Container Instances, Azure Database for MySQL
- **DigitalOcean**: Droplets, Managed Databases

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   ```bash
   # Check MySQL container status
   docker-compose ps mysql
   
   # Check logs
   docker-compose logs mysql
   ```

2. **CORS Issues**
   ```bash
   # Verify CORS configuration in backend
   # Check API URL in frontend configuration
   ```

3. **Build Failures**
   ```bash
   # Clear npm cache
   npm cache clean --force
   
   # Clear Maven cache
   ./mvnw clean
   ```

## 📈 Monitoring

### Health Checks

- **Backend Health**: http://localhost:5454/actuator/health
- **Frontend Health**: http://localhost:3000/health
- **Database Status**: Check via admin tools or logs

### Logs

```bash
# View all logs
docker-compose logs

# Backend logs
docker-compose logs backend

# Frontend logs
docker-compose logs frontend

# Database logs
docker-compose logs mysql
```

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Backend Development**: Spring Boot, MySQL, API Design
- **Frontend Development**: React, TypeScript, UI/UX
- **DevOps**: Docker, CI/CD, Deployment
- **QA**: Testing, Security, Performance

## 📞 Support

For support and questions:
- **Documentation**: Check this README and API docs
- **Issues**: Create an issue on GitHub
- **Discussions**: Use GitHub Discussions

## 🗺️ Roadmap

### Upcoming Features
- [ ] Mobile App (React Native)
- [ ] Advanced Analytics Dashboard
- [ ] Inventory Management System
- [ ] Multi-language Support
- [ ] Social Media Integration
- [ ] Advanced Search with Elasticsearch
- [ ] Real-time Notifications
- [ ] Seller Subscription Plans

### Improvements
- [ ] Performance Optimization
- [ ] Enhanced Security Features
- [ ] Better Error Handling
- [ ] Automated Testing
- [ ] CI/CD Pipeline
- [ ] Monitoring & Alerting

---

**Made with ❤️ by the E-Commerce Team**