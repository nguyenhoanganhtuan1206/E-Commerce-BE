package com.ecommerce.api.user.dto;

import com.ecommerce.api.location.dto.LocationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateResponseDTO {

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private Instant updatedAt;
}
