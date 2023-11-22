package com.ecommerce.persistent.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    Optional<CartEntity> findByUserIdAndSellerId(final UUID userId, final UUID sellerId);

    List<CartEntity> findByUserId(final UUID userId);
}
