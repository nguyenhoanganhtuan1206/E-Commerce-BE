package com.ecommerce.persistent.variant;

import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CategoryVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String variantName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "categoryVariant")
    private Set<ProductEntity> products;
}
