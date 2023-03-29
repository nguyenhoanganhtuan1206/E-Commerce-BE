package com.ecommerce.persistent.product;

import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private long price;

    private String condition;

    private boolean productApproval;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerEntity seller;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_variant_id", nullable = false)
    private CategoryVariantEntity categoryVariant;

    @OneToOne(mappedBy = "product", cascade = CascadeType.MERGE)
    private InventoryEntity inventoryEntity;
}
