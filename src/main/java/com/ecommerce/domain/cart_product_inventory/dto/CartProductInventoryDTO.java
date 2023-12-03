package com.ecommerce.domain.cart_product_inventory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartProductInventoryDTO {

    private UUID id;

    private int quantity;

    private long totalPrice;

    private UUID cartId;

    private UUID inventoryId;

    private UUID productId;
}
