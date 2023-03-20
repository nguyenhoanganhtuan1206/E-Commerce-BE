package com.ecommerce.domain.user;

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

    private SellerDTO seller;

    private Set<RoleDTO> roles;

    private Set<LocationDTO> locations;
}
