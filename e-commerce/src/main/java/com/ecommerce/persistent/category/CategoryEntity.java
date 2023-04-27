package com.ecommerce.persistent.category;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
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

    @OneToMany(mappedBy = "category")
    private Set<CategoryVariantEntity> categoryVariants;

    @OneToMany(mappedBy = "category")
    private Set<BrandEntity> brands;

    public CategoryEntity(String name) {
        this.categoryName = name;
    }
}
