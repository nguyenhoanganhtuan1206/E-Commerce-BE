package com.ecommerce.persistent.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<UserEntity> findByEmail(final String email);

    Optional<UserEntity> findByCodeResetPassword(final String code);

    @Query("select u from UserEntity u " +
            "left join LocationEntity l " +
            "on l.user.id = u.id " +
            "where u.id = :userId " +
            "order by l.defaultLocation DESC , l.createdAt DESC ")
    Optional<UserEntity> findProfileById(final UUID userId);
}
