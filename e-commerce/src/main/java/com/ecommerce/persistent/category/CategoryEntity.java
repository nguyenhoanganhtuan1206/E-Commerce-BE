package com.ecommerce.persistent.category;

import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.variant.ProductVariantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<ProductEntity> products;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<ProductVariantEntity> productVariants;
}
