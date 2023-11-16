package com.ecommerce.persistent.cart_product_inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartProductInventoryRepository extends JpaRepository<CartProductInventoryEntity, UUID> {

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId) " +
            " AND cpi.productId = :productId")
    Optional<CartProductInventoryEntity> findByUserIdAndProductId(final UUID userId, final UUID productId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId)" +
            " AND cpi.inventoryId = :inventoryId")
    Optional<CartProductInventoryEntity> findByUserIdAndInventoryId(final UUID userId, final UUID inventoryId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT u FROM UserEntity u WHERE u.id = :userId) " +
            "AND cpi.productId IN (SELECT p FROM ProductEntity p WHERE p.seller.id = :sellerId)")
    List<CartProductInventoryEntity> findByUserIdAndSellerId(final UUID userId, final UUID sellerId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT u FROM UserEntity u WHERE u.id = :userId)")
    List<CartProductInventoryEntity> findByUserId(final UUID userId);
}
