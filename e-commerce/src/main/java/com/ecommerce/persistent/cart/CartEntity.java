package com.ecommerce.persistent.cart;

import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int quantity;

    private Instant createdAt;

    private Instant dateAdded;

    @ManyToOne
    private SellerEntity seller;

    @ManyToMany(mappedBy = "carts")
    private List<ProductEntity> products;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_cart",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "cart_id", referencedColumnName = "id")})
    private UserEntity user;
}
