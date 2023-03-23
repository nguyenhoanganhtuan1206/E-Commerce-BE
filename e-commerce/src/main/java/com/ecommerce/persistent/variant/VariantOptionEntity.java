package com.ecommerce.persistent.variant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "variants_option")
@Getter
@Setter
@NoArgsConstructor
public class VariantOptionEntity {

    /**
     * @ Small, Large or Macbook, Lenovo
     */

    @ManyToOne(fetch = FetchType.LAZY)
    public ProductVariantEntity productVariant;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String variantValue;
    private int quantity;
}
