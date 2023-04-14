package com.ecommerce.persistent.seller;

import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.status.Status;
import com.ecommerce.persistent.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@Getter
@Setter
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

    @OneToMany(mappedBy = "seller")
    private Set<ProductEntity> products;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinTable(name = "user_seller",
            joinColumns = {@JoinColumn(name = "seller_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private UserEntity user;

    @OneToMany(mappedBy = "seller")
    private Set<CartEntity> carts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "payment_method_seller",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private Set<PaymentMethodEntity> paymentMethods;
}
