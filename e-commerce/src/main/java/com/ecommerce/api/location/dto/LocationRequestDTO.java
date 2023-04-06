package com.ecommerce.api.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationRequestDTO {

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

    private boolean defaultLocation;
}
