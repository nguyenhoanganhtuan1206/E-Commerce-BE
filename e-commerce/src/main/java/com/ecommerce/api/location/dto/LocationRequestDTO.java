package com.ecommerce.api.location.dto;

import com.ecommerce.domain.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LocationRequestDTO {

    /**
     * @ DTO has represents all fields from LocationEntity
     */

    private UUID id;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 3, message = "Address is invalid")
    private String address;

    @NotBlank(message = "City cannot be empty")
    @Size(min = 3, message = "City is invalid")
    private String city;

    @NotBlank(message = "District cannot be empty")
    @Size(min = 3, message = "District is invalid")
    private String district;

    @NotBlank(message = "Commune cannot be empty")
    @Size(min = 3, message = "Commune is invalid")
    private String commune;

    private UserDTO userDTO;
}
