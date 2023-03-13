package com.ecommerce.api.user.dto;

import com.ecommerce.api.location.dto.LocationDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserDTO {

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private Set<LocationDTO> locations;
}
