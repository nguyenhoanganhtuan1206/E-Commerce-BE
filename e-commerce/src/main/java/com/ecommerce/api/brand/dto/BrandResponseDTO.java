package com.ecommerce.api.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandResponseDTO {

    private UUID id;

    private String brandName;
}
