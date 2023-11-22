package com.ecommerce.persistent.cart_product_inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartProductInventoryRepository extends JpaRepository<CartProductInventoryEntity, UUID> {

    List<CartProductInventoryEntity> findByCartId(final UUID cartId);

    Optional<CartProductInventoryEntity> findByCartIdAndProductId(final UUID cartId, final UUID productId);

    Optional<CartProductInventoryEntity> findByCartIdAndInventoryId(final UUID cartId, final UUID inventoryId);

//    Optional<CartProductInventoryEntity> findByCartIdAndInventoryIdOrProductId(final UUID cartId, final UUID itemId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId) " +
            "AND cpi.productId = :productId " +
            "AND cpi.productId IN (SELECT p.id FROM ProductEntity p WHERE p.seller.id = :sellerId)")
    Optional<CartProductInventoryEntity> findByUserIdAndSellerIdAndProductId(final UUID userId, final UUID sellerId, final UUID productId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId) " +
            "AND cpi.inventoryId IN (SELECT iv.id FROM InventoryEntity iv WHERE iv.product.seller.id = :sellerId)")
    Optional<CartProductInventoryEntity> findByUserIdAndSellerId(final UUID userId, final UUID sellerId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c FROM CartEntity c WHERE c.user.id = :userId)")
    List<CartProductInventoryEntity> findByUserId(final UUID userId);
}
