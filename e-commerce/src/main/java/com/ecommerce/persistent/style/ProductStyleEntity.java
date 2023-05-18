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

    @ManyToMany
    @JoinTable(name = "product_product_style",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_style_id"))
    private List<ProductEntity> products;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    public ProductStyleEntity(final String name) {
        this.name = name;
    }
}
