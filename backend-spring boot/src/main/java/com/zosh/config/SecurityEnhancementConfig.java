package com.zosh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * Additional security configurations for enhanced protection
 */
@Configuration
public class SecurityEnhancementConfig {

    /**
     * Configure HTTP Firewall with strict settings
     */
    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        
        // Reject suspicious URLs
        firewall.setAllowUrlEncodedSlash(false);
        firewall.setAllowUrlEncodedPercent(false);
        firewall.setAllowUrlEncodedPeriod(false);
        firewall.setAllowBackSlash(false);
        firewall.setAllowSemicolon(false);
        
        // Reject requests with null bytes
        firewall.setAllowNull(false);
        
        // Only allow standard HTTP methods
        firewall.setUnsafeAllowAnyHttpMethod(false);
        
        return firewall;
    }
}
