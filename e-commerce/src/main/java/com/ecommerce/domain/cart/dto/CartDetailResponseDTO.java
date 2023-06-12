package com.ecommerce.domain.cart.dto;

import com.ecommerce.domain.inventory.dto.InventoryCartResponseDTO;
import com.ecommerce.domain.product.dto.ProductCartDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartDetailResponseDTO {

    private UUID id;

    private int quantity;

    private double totalPrice;

    private Instant createdAt;

    private ProductCartDetailDTO product;

    private InventoryCartResponseDTO inventory;
}
