package com.ecommerce.domain.location;

import com.ecommerce.domain.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

    /**
     * @ DTO has represents all fields from locationEntity
     */

    private UUID id;

    private String address;

    private String city;

    private String district;

    private String commune;

    private UserDTO user;
}
