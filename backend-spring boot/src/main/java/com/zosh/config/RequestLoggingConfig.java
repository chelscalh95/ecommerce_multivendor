package com.zosh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Request logging configuration for security monitoring
 */
@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(false); // Don't log sensitive payload data
        loggingFilter.setIncludeHeaders(false); // Don't log sensitive headers
        loggingFilter.setMaxPayloadLength(0); // No payload logging
        loggingFilter.setAfterMessagePrefix("REQUEST DATA: ");
        return loggingFilter;
    }
}
