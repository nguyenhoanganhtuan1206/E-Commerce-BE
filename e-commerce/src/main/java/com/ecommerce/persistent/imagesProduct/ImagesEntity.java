package com.ecommerce.persistent.imagesProduct;

import com.ecommerce.persistent.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_variant_id", nullable = false)
    private ProductEntity product;
}
