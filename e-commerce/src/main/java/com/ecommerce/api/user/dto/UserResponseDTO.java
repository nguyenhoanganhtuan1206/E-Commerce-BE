package com.ecommerce.api.user.dto;

import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.seller.SellerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {

    /**
     * @ this class has represents all fields from UserDTO without PASSWORD
     */

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private Instant createdAt;

    private Instant updatedAt;

    private String address;

    private SellerDTO seller;

    private Set<RoleDTO> roles;

    private Set<LocationDTO> locations;
}

