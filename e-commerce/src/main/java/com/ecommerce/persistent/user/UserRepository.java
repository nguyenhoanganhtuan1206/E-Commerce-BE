package com.ecommerce.persistent.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(final String email);

    @Query("select u from UserEntity u " +
            "inner join LocationEntity l " +
            "on l.user.id = u.id " +
            "where u.id = :userId " +
            "order by l.defaultLocation DESC , l.createdAt DESC ")
    Optional<UserEntity> findProfileById(final UUID userId);
}
