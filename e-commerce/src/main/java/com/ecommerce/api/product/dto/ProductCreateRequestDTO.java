package com.ecommerce.api.product.dto;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.domain.seller.SellerDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDTO {

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 6, max = 255, message = "Product name must be at between 6 to 255 characters")
    private String name;

    @NotBlank(message = "Price cannot be empty")
    @Min(value = 5, message = "Price must be at least $5")
    @Max(value = 1000000, message = "Price must be less than $1000000")
    private long price;

    @NotBlank(message = "Price cannot be empty")
    private String condition;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 6, max = 255, message = "Product description must be at between 6 to 255 characters")
    private String description;

    private UUID categoryId;

    private CategoryDTO categoryDTO;

    private SellerDTO seller;
}
