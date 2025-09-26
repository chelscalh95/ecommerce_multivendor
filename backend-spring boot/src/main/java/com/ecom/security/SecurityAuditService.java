package com.ecom.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Security audit service for logging security events
 */
@Service
public class SecurityAuditService {

    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");

    /**
     * Log successful authentication
     */
    public void logSuccessfulAuthentication(String email, String userAgent, String ipAddress) {
        securityLogger.info("SUCCESSFUL_LOGIN: email={}, userAgent={}, ip={}, timestamp={}", 
            email, userAgent, ipAddress, LocalDateTime.now());
    }

    /**
     * Log failed authentication attempt
     */
    public void logFailedAuthentication(String email, String reason, String userAgent, String ipAddress) {
        securityLogger.warn("FAILED_LOGIN: email={}, reason={}, userAgent={}, ip={}, timestamp={}", 
            email, reason, userAgent, ipAddress, LocalDateTime.now());
    }

    /**
     * Log access denied events
     */
    public void logAccessDenied(String email, String resource, String action) {
        securityLogger.warn("ACCESS_DENIED: email={}, resource={}, action={}, timestamp={}", 
            email, resource, action, LocalDateTime.now());
    }

    /**
     * Log password changes
     */
    public void logPasswordChange(String email, String ipAddress) {
        securityLogger.info("PASSWORD_CHANGED: email={}, ip={}, timestamp={}", 
            email, ipAddress, LocalDateTime.now());
    }

    /**
     * Log suspicious activity
     */
    public void logSuspiciousActivity(String email, String activity, String details) {
        securityLogger.error("SUSPICIOUS_ACTIVITY: email={}, activity={}, details={}, timestamp={}", 
            email, activity, details, LocalDateTime.now());
    }

    /**
     * Log privilege escalation attempts
     */
    public void logPrivilegeEscalation(String email, String attemptedRole, String currentRole) {
        securityLogger.error("PRIVILEGE_ESCALATION_ATTEMPT: email={}, attempted_role={}, current_role={}, timestamp={}", 
            email, attemptedRole, currentRole, LocalDateTime.now());
    }
}
