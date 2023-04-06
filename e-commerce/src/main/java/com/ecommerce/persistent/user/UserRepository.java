package com.ecommerce.persistent.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(final String email);

    @Query("SELECT u " +
            "FROM UserEntity u " +
            "INNER JOIN LocationEntity l " +
            "ON u.id = l.user.id " +
            "WHERE u.id = :userId " +
            "ORDER BY l.defaultLocation DESC")
    Optional<UserEntity> findUserByUserIdAndDefaultLocation(final UUID userId);
}
