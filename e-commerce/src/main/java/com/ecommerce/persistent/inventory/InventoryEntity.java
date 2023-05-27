package com.ecommerce.persistent.inventory;

import com.ecommerce.persistent.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String colorName;

    private String colorValue;

    private String sizeName;

    private String sizeValue;

    private int quantity;

    private long price;

    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
