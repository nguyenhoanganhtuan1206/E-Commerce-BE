package com.ecommerce.persistent.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    List<CartEntity> findByUserId(final UUID userId);

    @Query("select c from CartEntity c where c.user.id = :userId and c.product.id = :productId")
    Optional<CartEntity> findByUserIdAndProductId(final UUID userId, final UUID productId);

    @Query("select c from CartEntity c where c.user.id = :userId and c.inventory.id  = :inventoryId ")
    Optional<CartEntity> findByUserIdAndInventoryId(final UUID userId, final UUID inventoryId);
}
