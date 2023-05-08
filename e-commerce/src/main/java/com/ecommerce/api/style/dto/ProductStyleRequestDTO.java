package com.ecommerce.api.style.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductStyleRequestDTO {

    @NotBlank(message = "Product Style cannot be empty")
    @Size(max = 50, message = "Product Style must be at less than 50 characters")
    private String name;
}
