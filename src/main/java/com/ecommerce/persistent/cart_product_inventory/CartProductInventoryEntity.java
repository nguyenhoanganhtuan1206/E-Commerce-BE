package com.ecommerce.persistent.cart_product_inventory;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cart_product_inventory")
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int quantity;

    private UUID cartId;

    private UUID inventoryId;

    private UUID productId;
}