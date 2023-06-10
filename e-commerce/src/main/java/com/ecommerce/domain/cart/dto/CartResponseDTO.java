package com.ecommerce.domain.cart.dto;

import com.ecommerce.domain.inventory.dto.InventoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDTO {

    private UUID id;

    private int quantity;

    private double totalPrice;

    private Instant createdAt;

    private UUID productId;

    private InventoryDTO inventory;
}
