package com.ecommerce.api.location.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LocationResponseDTO {

    private UUID id;

    private String address;

    private String province;

    private String district;

    private String commune;

    private boolean defaultLocation;

    private UUID userId;
}
