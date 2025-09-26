package com.zosh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiting configuration to prevent abuse
 */
@Configuration
public class RateLimitingConfig {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Bean
    public OncePerRequestFilter rateLimitingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                    FilterChain filterChain) throws ServletException, IOException {
                
                String clientIp = getClientIpAddress(request);
                String requestPath = request.getRequestURI();
                
                // Apply stricter limits for auth endpoints
                Bucket bucket;
                if (requestPath.startsWith("/auth/")) {
                    bucket = getOrCreateAuthBucket(clientIp);
                } else {
                    bucket = getOrCreateGeneralBucket(clientIp);
                }
                
                if (bucket.tryConsume(1)) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(429); // Too Many Requests
                    response.getWriter().write("Rate limit exceeded. Please try again later.");
                }
            }
        };
    }

    private Bucket getOrCreateAuthBucket(String clientIp) {
        return buckets.computeIfAbsent("auth_" + clientIp, key -> {
            // 5 requests per minute for auth endpoints
            Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
            return Bucket4j.builder().addLimit(limit).build();
        });
    }

    private Bucket getOrCreateGeneralBucket(String clientIp) {
        return buckets.computeIfAbsent("general_" + clientIp, key -> {
            // 100 requests per minute for general endpoints
            Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));
            return Bucket4j.builder().addLimit(limit).build();
        });
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
}
