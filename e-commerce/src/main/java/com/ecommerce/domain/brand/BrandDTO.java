package com.ecommerce.domain.brand;

import com.ecommerce.api.category.CategoryDTO;
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

    private CategoryDTO categoryDTO;
}
