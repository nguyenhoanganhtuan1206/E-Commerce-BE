package com.ecommerce.api.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class UserLoginResponseDTO {

    private final UUID userId;

    private final String email;

    private final String username;

    private final Set<String> roles;

    private final String token;
}
