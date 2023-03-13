package com.ecommerce.api.location.dto;

import com.ecommerce.api.user.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Builder
@Getter
@Setter
@With
public class LocationDTO {

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
