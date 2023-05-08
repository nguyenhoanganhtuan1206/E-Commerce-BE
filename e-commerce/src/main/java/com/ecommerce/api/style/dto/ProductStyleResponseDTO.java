package com.ecommerce.api.style.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductStyleResponseDTO {

    private UUID id;

    private String name;
}
