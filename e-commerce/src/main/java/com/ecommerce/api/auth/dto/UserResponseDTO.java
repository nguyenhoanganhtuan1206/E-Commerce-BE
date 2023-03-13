package com.ecommerce.api.auth.dto;

import com.ecommerce.api.location.dto.LocationDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class UserResponseDTO {

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private Set<LocationDTO> locations;
}
