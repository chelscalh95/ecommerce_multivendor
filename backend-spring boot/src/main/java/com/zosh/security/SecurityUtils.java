package com.zosh.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Security utility class for common security validations
 */
@Component
public class SecurityUtils {

    /**
     * Get the current authenticated user's email
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Check if the current user has admin role
     */
    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Check if the current user has seller role
     */
    public static boolean isSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ROLE_SELLER"));
    }

    /**
     * Check if the current user has customer role
     */
    public static boolean isCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"));
    }

    /**
     * Check if the current user owns the resource (by email comparison)
     */
    public static boolean isOwner(String resourceOwnerEmail) {
        String currentUserEmail = getCurrentUserEmail();
        return currentUserEmail != null && currentUserEmail.equals(resourceOwnerEmail);
    }

    /**
     * Check if the current user can access seller resources
     */
    public static boolean canAccessSellerResources(String sellerEmail) {
        return isAdmin() || (isSeller() && isOwner(sellerEmail));
    }

    /**
     * Check if the current user can access customer resources
     */
    public static boolean canAccessCustomerResources(String customerEmail) {
        return isAdmin() || (isCustomer() && isOwner(customerEmail));
    }
}
