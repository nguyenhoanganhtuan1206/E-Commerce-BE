package com.ecommerce.persistent.location;

import com.ecommerce.persistent.user.UserEntity;
import lombok.*;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String address;

    private String province;

    private String district;

    private String commune;

    @Column(name = "created_at")
    private Instant createdAt;

    private Instant updatedAt;

    @Column(name = "default_location")
    private boolean defaultLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
