package com.ecommerce.api.product.dto;

import com.ecommerce.api.inventory.InventoryCreateRequestDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDTO {

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 6, max = 255, message = "Product name must be at between 6 to 255 characters")
    private String name;

    private long price;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 6, max = 255, message = "Product description must be at between 6 to 255 characters")
    private String description;

    @Max(value = 10000, message = "Quantity cannot large than 10000 quantities")
    private long quantity = 0;

    @NotBlank(message = "Variant name cannot be empty")
    private String variantName;

    @NotBlank(message = "Brand name cannot be empty")
    private String brandName;

    private List<String> paymentMethods;

    private List<String> categories;

    private List<InventoryCreateRequestDTO> inventories;

    private List<String> productStyles;
}
