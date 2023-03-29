package com.ecommerce.api.variant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryVariantResponseDTO {

    private UUID id;

    private String variantName;
}
