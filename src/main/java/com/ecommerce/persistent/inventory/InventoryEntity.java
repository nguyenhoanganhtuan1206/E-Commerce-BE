package com.ecommerce.persistent.inventory;

import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "color_name")
    private String colorName;

    @Column(name = "color_value")
    private String colorValue;

    @Column(name = "size_name")
    private String sizeName;

    @Column(name = "size_value")
    private String sizeValue;

    private int quantity;

    private long price;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.REMOVE)
    private List<CartEntity> carts;
}
