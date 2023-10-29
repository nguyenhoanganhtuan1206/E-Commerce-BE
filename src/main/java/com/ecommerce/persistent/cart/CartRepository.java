package com.ecommerce.persistent.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    @Query("select c from CartEntity c where c.paymentOrder is null")
    List<CartEntity> findByUserIdAndUnOrder(final UUID userId);

    @Query("select c from CartEntity c where c.user.id = :userId and c.product.id = :productId and c.paymentOrder is null")
    Optional<CartEntity> findByUserIdAndProductIdWithoutPaymentOrder(final UUID userId, final UUID productId);

    @Query("select c from CartEntity c where c.user.id = :userId and c.inventory.id  = :inventoryId and c.paymentOrder is null ")
    Optional<CartEntity> findByUserIdAndInventoryIdWithoutPaymentOrder(final UUID userId, final UUID inventoryId);

    @Query("select c from CartEntity c " +
            " where c.inventory.product.seller.id = :sellerId and c.user.id = :userId and c.paymentOrder is null ")
    List<CartEntity> findBySellerIdByUserIdAndExistedInventoryWithoutPaymentOrder(final UUID userId, final UUID sellerId);

    @Query("select c from CartEntity c " +
            " where c.product.seller.id = :sellerId and c.user.id = :userId" +
            " and c.paymentOrder is null ")
    List<CartEntity> findBySellerIdByUserIdAndExistedProductWithoutPaymentOrder(final UUID userId, final UUID sellerId);
}
