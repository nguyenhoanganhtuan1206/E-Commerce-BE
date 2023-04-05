package com.ecommerce.domain.inventory;

import com.ecommerce.domain.product.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class InventoryDTO {

    private UUID id;

    private int quantity;

    private Instant createdAt;

    private Instant updatedAt;

    private ProductDTO product;

    public InventoryDTO(int quantity) {
        this.quantity = quantity;
    }
}
