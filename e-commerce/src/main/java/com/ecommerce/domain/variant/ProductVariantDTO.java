package com.ecommerce.domain.variant;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductVariantDTO {

    private UUID id;

    private String variantName;

    private String variantValue;

    private long quantity;

    private CategoryDTO category;

    private ProductDTO product;
}
