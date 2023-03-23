package com.ecommerce.api.category;

import com.ecommerce.domain.variant.ProductVariantDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private UUID id;

    private String categoryName;

    private Set<ProductVariantDTO> productVariants;
}
