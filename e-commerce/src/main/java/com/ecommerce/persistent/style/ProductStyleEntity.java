package com.ecommerce.persistent.style;

import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_style")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStyleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "productStyles")
    private List<ProductEntity> products;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    public ProductStyleEntity(final String name) {
        this.name = name;
    }
}
