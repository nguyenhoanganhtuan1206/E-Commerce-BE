package com.ecommerce.domain.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

    private UUID id;

    private String address;

    private String province;

    private String district;

    private String commune;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean defaultLocation;

    private UUID userId;
}
