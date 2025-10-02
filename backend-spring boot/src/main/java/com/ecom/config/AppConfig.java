package com.ecom.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AppConfig {

    private final RateLimitingConfig rateLimitingConfig;

    public AppConfig(RateLimitingConfig rateLimitingConfig) {
        this.rateLimitingConfig = rateLimitingConfig;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(Authorize -> Authorize
                        // Public endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/products/search").permitAll()
                        .requestMatchers("/api/products/{id}").permitAll()
                        .requestMatchers("/api/products/{id}/reviews").permitAll()
                        .requestMatchers("/api/products/category/{categoryId}").permitAll()
                        .requestMatchers("/api/products/public/**").permitAll()
                        .requestMatchers("/api/home/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        
                        // Admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/coupons/admin/**").hasRole("ADMIN")
                        
                        // Seller endpoints
                        .requestMatchers("/sellers/**").hasAnyRole("SELLER", "ADMIN")
                        .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
                        .requestMatchers("/api/seller-products/**").hasRole("SELLER")
                        .requestMatchers("/api/seller/revenue/**").hasRole("SELLER")
                        
                        // Customer endpoints
                        .requestMatchers("/api/users/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/cart/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/orders/**").hasAnyRole("CUSTOMER", "SELLER", "ADMIN")
                        .requestMatchers("/api/wishlist/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/reviews/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/payment/**").hasRole("CUSTOMER")
                        
                        // Any other authenticated endpoint
                        .anyRequest().authenticated()
                )
                .addFilterBefore(rateLimitingConfig.rateLimitingFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                // Security Headers
                .headers(headers -> headers
                    .frameOptions(frameOptions -> frameOptions.deny())
                    .contentTypeOptions(contentTypeOptions -> {})
                    .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                        .maxAgeInSeconds(31536000)
                        .includeSubDomains(true)
                        .preload(true)
                    )
                    .referrerPolicy(referrerPolicy -> referrerPolicy
                        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    )
                );
               
		
		return http.build();
		
	}
	
    // CORS Configuration
    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(Arrays.asList(
                "https://zosh-bazzar-zosh.vercel.app",
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:5454"
            ));
            cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            cfg.setAllowCredentials(true);
            cfg.setAllowedHeaders(Arrays.asList("*"));
            cfg.setExposedHeaders(Arrays.asList("Authorization"));
            cfg.setMaxAge(3600L);
            return cfg;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
