package com.ecommerce.domain.brand;

import com.ecommerce.domain.category.CategoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandDTO {

    /**
     * @ DTO has represents all fields from BrandDTO
     */

    private UUID id;

    private String brandName;

    private CategoryDTO category;
}
