package com.ecommerce.api.category.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CategoryResponseDTO {

    private UUID id;

    private String categoryName;
}
