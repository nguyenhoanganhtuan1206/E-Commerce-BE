package com.ecommerce.api.product.dto;

import com.ecommerce.domain.category.CategoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDTO {

    private UUID id;

    private String name;

    private double price;

    private String condition;

    private boolean productApproval;

    private String description;

    private CategoryDTO category;
}
