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

    private int quantity;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.ALL})
    @JoinTable(name = "inventory_product",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "inventory_id", referencedColumnName = "id")})
    private ProductEntity product;
}
