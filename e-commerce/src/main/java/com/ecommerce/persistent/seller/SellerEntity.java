package com.ecommerce.persistent.seller;

import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.status.Status;
import com.ecommerce.persistent.style.ProductStyleEntity;
import com.ecommerce.persistent.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String sellerName;

    private String emailSeller;

    private String phoneNumber;

    private float sellerRating;

    private String address;

    private String province;

    private String district;

    private String commune;

    private Instant createdAt;

    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    private Status sellerApproval;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<ProductEntity> products;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "seller")
    private Set<CartEntity> carts;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<ProductStyleEntity> productStyles;
}
