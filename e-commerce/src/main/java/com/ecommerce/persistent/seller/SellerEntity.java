package com.ecommerce.persistent.seller;

import com.ecommerce.persistent.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private boolean sellerStatus;

    private String address;

    private String city;

    private String district;

    private String commune;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_seller",
            joinColumns = {@JoinColumn(name = "seller_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private UserEntity user;
}
