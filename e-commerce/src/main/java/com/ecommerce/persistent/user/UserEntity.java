package com.ecommerce.persistent.user;

import com.ecommerce.persistent.location.LocationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

    @Entity
    @Table(name = "users")
    @Getter
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

        private String password;

        @OneToMany(mappedBy = "user")
        private Set<LocationEntity> locations;
    }
