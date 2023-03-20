package com.ecommerce.api.location.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class LocationResponseDTO {

    private UUID id;

    private String address;

    private String city;

    private String district;

    private String commune;
}
