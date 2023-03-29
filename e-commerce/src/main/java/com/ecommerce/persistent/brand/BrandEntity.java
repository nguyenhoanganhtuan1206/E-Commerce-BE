package com.ecommerce.persistent.brand;

import com.ecommerce.persistent.category.CategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String brandName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.MERGE})
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public BrandEntity(final String brandName) {
        this.brandName = brandName;
    }
}
