# Multi-stage build for optimized React frontend

# Build stage
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Accept build arguments
ARG REACT_APP_DOCKER=false
ARG REACT_APP_API_URL

# Set environment variables for build
ENV REACT_APP_DOCKER=$REACT_APP_DOCKER
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Build the application
RUN npm run build

# Production stage
FROM nginx:1.25-alpine

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built app from builder stage
COPY --from=builder /app/build /usr/share/nginx/html

# Create a non-root user for security
RUN addgroup -g 1001 -S nodejs && \
    adduser -S reactuser -u 1001

# Set ownership of nginx files
RUN chown -R reactuser:nodejs /usr/share/nginx/html && \
    chown -R reactuser:nodejs /var/cache/nginx && \
    chown -R reactuser:nodejs /var/log/nginx && \
    chown -R reactuser:nodejs /etc/nginx/conf.d

# Create nginx pid directory
RUN mkdir -p /var/run/nginx && \
    chown -R reactuser:nodejs /var/run/nginx

# Switch to non-root user
USER reactuser

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:3000/ || exit 1

# Expose port 3000 (nginx will serve on this port)
EXPOSE 3000

# Start nginx
CMD ["nginx", "-g", "daemon off;"]