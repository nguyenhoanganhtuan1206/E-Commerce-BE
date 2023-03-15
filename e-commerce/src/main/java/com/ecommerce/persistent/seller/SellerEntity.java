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

    private float sellerString;

    private boolean sellerStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
