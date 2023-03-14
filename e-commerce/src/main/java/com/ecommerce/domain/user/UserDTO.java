package com.ecommerce.domain.user;

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
public class UserDTO {

    /**
     * @ DTO has represents all fields from UserEntity
     */

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private Instant createdAt;

    private Instant updatedAt;

    private String password;

    private Set<LocationDTO> locations;
}
