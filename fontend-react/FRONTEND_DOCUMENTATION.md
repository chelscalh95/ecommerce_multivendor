# E-commerce Multi-Vendor Frontend Documentation

## Project Overview
This is a React TypeScript frontend application for a multi-vendor e-commerce platform. The application provides interfaces for customers, sellers, and administrators with comprehensive features for online marketplace operations.

## Technology Stack

### Core Technologies
- **React 18.3.1** - Frontend framework
- **TypeScript 4.9.5** - Type-safe JavaScript
- **Material-UI (MUI) 5.16.0** - UI component library
- **Redux Toolkit 2.2.6** - State management
- **React Router Dom 6.24.1** - Client-side routing
- **Axios 1.7.2** - HTTP client for API calls

### Styling & UI
- **Tailwind CSS 3.4.4** - Utility-first CSS framework
- **Material-UI (MUI) Components**:
  - @mui/material - Core components
  - @mui/icons-material - Icon library
  - @mui/x-date-pickers - Date picker components
- **React Slick 0.30.2** - Carousel component
- **Slick Carousel 1.8.1** - Carousel functionality

### Form Management & Validation
- **Formik 2.4.6** - Form library
- **Yup 1.4.0** - Schema validation

### Utilities
- **Day.js 1.11.13** - Date manipulation
- **Recharts 2.12.7** - Chart components for analytics
- **Redux Thunk 3.1.0** - Async Redux actions

### Development & Testing
- **React Scripts 5.0.1** - Build tools and scripts
- **Testing Library** - Unit testing framework
- **TypeScript Types** - Type definitions for libraries

## Project Structure

```
fontend-react/
├── public/                 # Static assets
│   ├── images/            # Product and banner images
│   └── manifest.json      # PWA manifest
├── src/
│   ├── Config/            # API configuration
│   ├── Redux Toolkit/     # State management
│   │   ├── Admin/         # Admin-specific slices
│   │   ├── Customer/      # Customer-specific slices
│   │   ├── Seller/        # Seller-specific slices
│   │   └── Store.ts       # Redux store configuration
│   ├── Theme/             # Custom MUI theme
│   ├── admin/             # Admin interface components
│   ├── customer/          # Customer interface components
│   ├── seller/            # Seller interface components
│   ├── data/              # Static data and mock data
│   ├── routes/            # Route configurations
│   ├── types/             # TypeScript type definitions
│   └── util/              # Utility functions
├── Dockerfile             # Docker configuration
├── docker-compose.yml     # Docker compose setup
├── nginx.conf             # Nginx configuration
├── package.json           # Dependencies and scripts
├── tailwind.config.js     # Tailwind CSS configuration
└── tsconfig.json          # TypeScript configuration
```

## Features by User Role

### Customer Features
- **Authentication**: Login, signup, OTP verification, password reset
- **Product Browsing**: Category-wise browsing, search, filters
- **Shopping Cart**: Add/remove items, quantity management
- **Wishlist**: Save favorite products
- **Checkout**: Address management, payment processing
- **Order Management**: Order history, tracking, reviews
- **AI ChatBot**: Product recommendations and assistance
- **Profile Management**: Personal details, addresses, saved cards

### Seller Features
- **Authentication**: Seller registration and login
- **Dashboard**: Sales analytics, revenue charts
- **Product Management**: Add, update, delete products
- **Inventory Management**: Stock tracking
- **Order Management**: Process orders, update status
- **Financial Management**: View payouts, transactions
- **Account Management**: Business details, bank information

### Admin Features
- **Dashboard**: System overview, analytics
- **User Management**: Manage customers and sellers
- **Product Management**: Oversee all products
- **Category Management**: Manage product categories
- **Coupon Management**: Create and manage discounts
- **Deal Management**: Feature products and deals
- **Order Oversight**: Monitor all transactions

## API Integration

### Backend Configuration
- **Base URL**: `http://localhost:5454` (development)
- **Deployed URL**: `https://zosh-bazzar-backend.onrender.com`
- **API Client**: Axios with interceptors for authentication

### API Endpoints Structure
```typescript
// Authentication
/auth/signup
/auth/signin
/auth/reset-password
/auth/sent/login-signup-otp

// Products
/products
/products/{id}
/products/search

// Cart & Orders
/api/cart
/api/orders

// Seller Operations
/sellers/products
/sellers/orders
/sellers/revenue

// Admin Operations
/admin/products
/admin/users
/admin/coupons
```

## State Management

### Redux Store Structure
```typescript
{
  auth: AuthState,
  products: ProductState,
  cart: CartState,
  orders: OrderState,
  wishlist: WishlistState,
  user: UserState,
  seller: SellerState,
  admin: AdminState,
  deals: DealState,
  coupons: CouponState,
  transactions: TransactionState,
  payouts: PayoutState,
  aiChatBot: AiChatBotState
}
```

### Key Redux Slices
- **AuthSlice**: User authentication and JWT management
- **ProductSlice**: Product data and search functionality
- **CartSlice**: Shopping cart operations
- **OrderSlice**: Order management and history
- **SellerSlice**: Seller-specific operations
- **AdminSlice**: Admin panel functionality

## Docker Configuration

### Multi-stage Build
The Dockerfile uses a multi-stage build process:
1. **Build Stage**: Node.js Alpine for building the React app
2. **Production Stage**: Nginx Alpine for serving static files

### Key Docker Features
- **Security**: Non-root user execution
- **Performance**: Optimized build with production dependencies only
- **Health Checks**: Container health monitoring
- **Volume Mounting**: Persistent logs

### Docker Compose Setup
- **Frontend Service**: React app served via Nginx
- **Backend Integration**: Links to Spring Boot backend
- **Network**: Shared Docker network for service communication
- **Port Mapping**: Frontend on port 3000

## Nginx Configuration

### Key Features
- **Gzip Compression**: Optimized asset delivery
- **Security Headers**: XSS protection, content type validation
- **API Proxy**: Routes API calls to backend service
- **Static Caching**: Long-term caching for assets
- **SPA Support**: Handles client-side routing
- **Health Check**: Built-in health endpoint

### Proxy Configuration
```nginx
# API proxy to backend
location /api/ {
    proxy_pass http://backend:5454;
    # Additional proxy headers and CORS
}

# Auth endpoints proxy
location /auth/ {
    proxy_pass http://backend:5454;
}
```

## Environment Configuration

### Development
- API URL: `http://localhost:5454`
- Hot reloading enabled
- Development optimizations

### Production
- Optimized build with minification
- Static asset caching
- Security headers
- Health monitoring

## Build and Deployment

### Available Scripts
```bash
npm start      # Development server
npm run build  # Production build
npm test       # Run tests
npm run eject  # Eject from Create React App
```

### Docker Commands
```bash
# Build frontend image
docker build -t ecommerce-frontend .

# Run with compose
docker-compose up -d

# View logs
docker-compose logs frontend
```

## Security Features

### Frontend Security
- **Input Validation**: Formik + Yup validation
- **XSS Protection**: React's built-in XSS prevention
- **JWT Handling**: Secure token storage and transmission
- **CORS Configuration**: Proper cross-origin setup

### Nginx Security
- **Security Headers**: X-Frame-Options, Content-Type-Options
- **File Access Control**: Prevents access to sensitive files
- **Rate Limiting**: Can be configured for DDoS protection

## Performance Optimizations

### Build Optimizations
- **Code Splitting**: Dynamic imports for route-based splitting
- **Tree Shaking**: Unused code elimination
- **Asset Optimization**: Image and bundle optimization
- **Caching**: Browser and CDN caching strategies

### Runtime Optimizations
- **Lazy Loading**: Component-level lazy loading
- **Memoization**: React.memo for component optimization
- **Virtual Scrolling**: For large product lists
- **Image Optimization**: Responsive images and lazy loading

## API Connection Status

### ✅ Working Connections
- Authentication endpoints
- Product management
- User operations
- Order processing

### ⚠️ Configuration Issues Found
1. **API URL Mismatch**: Frontend uses `localhost:5454` but Docker backend runs on `backend:5454`
2. **Proxy Configuration**: Nginx proxy needs alignment with API routes
3. **Environment Variables**: Missing environment-based API URL configuration

### 🔧 Recommended Fixes
1. Update API configuration to use environment variables
2. Align proxy routes with backend endpoints
3. Implement proper service discovery in Docker environment

## Troubleshooting

### Common Issues
1. **CORS Errors**: Check nginx proxy configuration
2. **API Connection**: Verify backend service is running
3. **Build Failures**: Check Node.js version compatibility
4. **Docker Issues**: Ensure proper network configuration

### Debug Commands
```bash
# Check container logs
docker logs ecommerce-frontend

# Inspect network
docker network inspect ecommerce-network

# Health check
curl http://localhost:3000/health
```

## Future Enhancements

### Planned Features
- **PWA Support**: Service worker implementation
- **Offline Functionality**: Cached data for offline browsing
- **Real-time Updates**: WebSocket integration
- **Advanced Analytics**: Enhanced reporting dashboard
- **Mobile App**: React Native version
- **Internationalization**: Multi-language support

### Performance Improvements
- **CDN Integration**: Global content delivery
- **Advanced Caching**: Redis integration
- **Database Optimization**: Query optimization
- **Image CDN**: Optimized image delivery

## Contributing Guidelines

### Development Setup
1. Clone repository
2. Install dependencies: `npm install`
3. Configure environment variables
4. Start development server: `npm start`

### Code Standards
- TypeScript strict mode
- ESLint configuration
- Prettier formatting
- Component-based architecture
- Redux Toolkit patterns

---

**Note**: This documentation covers the current state of the frontend application. For backend API documentation, refer to the Spring Boot backend documentation.