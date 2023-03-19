package com.ecommerce.domain.auth;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Getter
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final UUID userId;

    private final String email;

    private final String role;

    public UserAuthenticationToken(final UUID userId,
                                   final String email,
                                   Collection<? extends GrantedAuthority> authorities) {
        super(userId, email, authorities);
        this.userId = userId;
        this.email = email;
        this.role = this.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(null);
    }
}
