package com.ecommerce.persistent.variant;

import com.ecommerce.persistent.product.ProductEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "categoryVariant")
    private List<ProductEntity> products;

    public CategoryVariantEntity(final String name) {
        this.name = name;
    }
}
