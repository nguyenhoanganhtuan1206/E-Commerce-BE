package com.ecommerce.persistent.category;

import com.ecommerce.persistent.product.ProductEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@Builder
@With
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private List<ProductEntity> products;

    public CategoryEntity(String name) {
        this.categoryName = name;
    }
}
