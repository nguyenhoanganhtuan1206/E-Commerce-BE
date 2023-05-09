package com.ecommerce.domain.brand;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandDTO {

    private UUID id;

    private String brandName;
}
