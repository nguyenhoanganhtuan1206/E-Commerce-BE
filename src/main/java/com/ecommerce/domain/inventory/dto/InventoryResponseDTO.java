package com.ecommerce.domain.inventory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class InventoryResponseDTO {

    private UUID id;

    private String colorName;

    private String colorValue;

    private String sizeName;

    private String sizeValue;

    private int quantity;

    private long price;

    private Instant createdAt;

    private Instant updatedAt;

    private UUID productId;
}
