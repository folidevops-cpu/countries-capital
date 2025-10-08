package com.example.countrycapital.service;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.User;
import com.example.countrycapital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get current authenticated user (tenant)
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        String username = authentication.getName();
        if ("anonymousUser".equals(username)) {
            throw new RuntimeException("Anonymous user cannot access tenant resources");
        }
        
        Optional<User> user = userRepository.findByUsername(username);
        
        return user.orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
    
    /**
     * Get current tenant ID
     */
    public String getCurrentTenantId() {
        return getCurrentUser().getUsername();
    }
    
    /**
     * Check if user owns the resource
     */
    public boolean isCurrentUserOwner(User resourceOwner) {
        if (resourceOwner == null) {
            return false;
        }
        
        User currentUser = getCurrentUser();
        return currentUser.getId().equals(resourceOwner.getId());
    }
    
    /**
     * Check if current user owns the task
     */
    public boolean isCurrentUserOwner(Task task) {
        if (task == null || task.getUser() == null) {
            return false;
        }
        
        User currentUser = getCurrentUser();
        return currentUser.getId().equals(task.getUser().getId());
    }
    
    /**
     * Check if user owns the resource by tenant ID
     */
    public boolean isCurrentUserTenant(String tenantId) {
        if (tenantId == null) {
            return false;
        }
        
        return getCurrentTenantId().equals(tenantId);
    }
    
    /**
     * Check if current user is authenticated
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getName());
    }
    
    /**
     * Get current user ID (safe method that returns null if not authenticated)
     */
    public Long getCurrentUserId() {
        try {
            return getCurrentUser().getId();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get current username (safe method that returns null if not authenticated)
     */
    public String getCurrentUsername() {
        try {
            return getCurrentUser().getUsername();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get current user email (safe method that returns null if not authenticated)
     */
    public String getCurrentUserEmail() {
        try {
            return getCurrentUser().getEmail();
        } catch (Exception e) {
            return null;
        }
    }
}