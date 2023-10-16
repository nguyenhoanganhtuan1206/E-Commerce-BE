package com.ecommerce.api.style.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ProductStyleRequestDTO {

    @NotBlank(message = "Product Style cannot be empty")
    @Size(max = 50, message = "Product Style must be at less than 50 characters")
    private String name;
}
