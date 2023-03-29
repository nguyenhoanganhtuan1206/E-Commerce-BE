package com.ecommerce.api.category;

import com.ecommerce.domain.brand.BrandDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
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

    private Set<BrandDTO> brands;

    private Set<CategoryVariantDTO> categoryVariants;
}
