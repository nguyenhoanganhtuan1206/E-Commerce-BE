package com.ecommerce.domain.user;

import com.ecommerce.domain.role.RoleDTO;
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

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private Instant createdAt;

    private Instant updatedAt;

    private String password;

    private String address;

    private UUID sellerId;

    private Set<RoleDTO> roles;
}
