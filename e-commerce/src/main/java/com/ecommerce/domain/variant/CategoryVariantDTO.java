package com.ecommerce.domain.variant;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryVariantDTO {

    /**
     * @ DTO has represents all fields from ProductVariantEntity
     */

    private UUID id;

    private String variantName;

    private CategoryDTO category;

    private Set<ProductDTO> products;
}
