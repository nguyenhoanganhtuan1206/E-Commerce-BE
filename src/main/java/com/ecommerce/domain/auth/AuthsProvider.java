package com.ecommerce.domain.auth;

import com.ecommerce.error.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class AuthsProvider {

    public UserAuthenticationToken getCurrentAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UnauthorizedException("Authentication failed. Please ensure that you have provided the correct credentials.");
        }

        return (UserAuthenticationToken) authentication;
    }

    public UUID getCurrentUserId() {
        return getCurrentAuthentication().getUserId();
    }

    public String getCurrentUserEmail() {
        return getCurrentAuthentication().getEmail();
    }

    public Set<String> getCurrentUserRoles() {
        return getCurrentAuthentication().getRole();
    }
}
