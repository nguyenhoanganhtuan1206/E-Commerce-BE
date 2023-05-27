package com.ecommerce.domain.inventory.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {

    private String colorName;

    private String colorValue;

    private String sizeName;

    private String sizeValue;

    private int quantity;

    private long price;

    private Instant createdAt;

    private Instant updatedAt;
}
