package com.ecommerce.persistent.user;

import com.ecommerce.persistent.location.LocationEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private Instant createdAt;

    private Instant updatedAt;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<LocationEntity> locations;

    @OneToOne(mappedBy = "user")
    private SellerEntity seller;
}
