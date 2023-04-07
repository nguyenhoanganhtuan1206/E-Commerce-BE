package com.ecommerce.persistent.location;

import com.ecommerce.persistent.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "location")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String address;

    private String city;

    private String district;

    private String commune;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean defaultLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
