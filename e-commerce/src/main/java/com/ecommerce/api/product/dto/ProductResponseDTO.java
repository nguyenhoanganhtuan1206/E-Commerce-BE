package com.ecommerce.api.product.dto;

import com.ecommerce.persistent.category.CategoryEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private boolean productStatus;

    private String description;

    private CategoryEntity category;
}
