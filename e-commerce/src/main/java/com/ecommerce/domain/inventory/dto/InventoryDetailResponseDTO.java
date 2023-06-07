package com.ecommerce.domain.inventory.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDetailResponseDTO {

    private String colorName;

    private String sizeName;

    private List<String> colorValues;

    private List<String> sizeValues;

    private double price;

    private int quantity;
}
