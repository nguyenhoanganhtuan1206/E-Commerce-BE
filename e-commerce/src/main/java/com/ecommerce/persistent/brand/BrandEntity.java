package com.ecommerce.persistent.brand;

import com.ecommerce.persistent.product.ProductEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "brand")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String brandName;

    @OneToMany(mappedBy = "brand", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<ProductEntity> products;

    public BrandEntity(final String brandName) {
        this.brandName = brandName;
    }
}
