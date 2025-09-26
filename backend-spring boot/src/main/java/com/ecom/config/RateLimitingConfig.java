package com.ecom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple rate limiting configuration to prevent abuse
 */
@Configuration
public class RateLimitingConfig {

    private final ConcurrentHashMap<String, RequestTracker> trackers = new ConcurrentHashMap<>();

    @Bean
    public OncePerRequestFilter rateLimitingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                    FilterChain filterChain) throws ServletException, IOException {
                
                String clientIp = getClientIpAddress(request);
                String requestPath = request.getRequestURI();
                
                // Apply stricter limits for auth endpoints
                int maxRequests = requestPath.startsWith("/auth/") ? 10 : 100;
                
                if (isRateLimited(clientIp, maxRequests)) {
                    response.setStatus(429); // Too Many Requests
                    response.setHeader("Retry-After", "60"); // Retry after 60 seconds
                    response.getWriter().write("Rate limit exceeded. Please try again later.");
                    return;
                }
                
                filterChain.doFilter(request, response);
            }
        };
    }

    private boolean isRateLimited(String clientIp, int maxRequests) {
        RequestTracker tracker = trackers.computeIfAbsent(clientIp, k -> new RequestTracker());
        return tracker.isRateLimited(maxRequests);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private static class RequestTracker {
        private int requestCount = 0;
        private LocalDateTime windowStart = LocalDateTime.now();
        private static final int WINDOW_SIZE_MINUTES = 1;

        public synchronized boolean isRateLimited(int maxRequests) {
            LocalDateTime now = LocalDateTime.now();
            
            // Reset window if enough time has passed
            if (ChronoUnit.MINUTES.between(windowStart, now) >= WINDOW_SIZE_MINUTES) {
                requestCount = 0;
                windowStart = now;
            }
            
            requestCount++;
            return requestCount > maxRequests;
        }
    }
}
