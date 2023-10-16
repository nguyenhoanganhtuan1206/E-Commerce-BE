package com.ecommerce.api.location.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class LocationRequestDTO {

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 3, message = "Address is invalid")
    private String address;

    @NotBlank(message = "Province cannot be empty")
    @Size(min = 3, message = "Province is invalid")
    private String province;

    @NotBlank(message = "District cannot be empty")
    @Size(min = 3, message = "District is invalid")
    private String district;

    @NotBlank(message = "Commune cannot be empty")
    @Size(min = 3, message = "Commune is invalid")
    private String commune;

    private boolean defaultLocation;
}
