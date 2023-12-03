package com.ecommerce.persistent.cart_product_inventory;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_status.PaymentStatus;
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

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.sellerId = :sellerId)")
    List<CartProductInventoryEntity> findBySellerId(final UUID sellerId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi WHERE cpi.cartId IN (SELECT p.cartId FROM PaymentOrderEntity p WHERE p.id = :paymentOrderId)")
    List<CartProductInventoryEntity> findByPaymentOrderId(final UUID paymentOrderId);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId AND c.isPayment = :isPayment)")
    List<CartProductInventoryEntity> findByUserIdAndStatusPayment(final UUID userId, final boolean isPayment);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId AND c.isPayment = true) AND " +
            "cpi.cartId IN (SELECT p.cartId FROM PaymentOrderEntity p WHERE p.paymentStatus = :paymentStatus)")
    List<CartProductInventoryEntity> findByUserIdAndPaidAndPaymentStatus(final UUID userId, final PaymentStatus paymentStatus);

    @Query("SELECT cpi FROM CartProductInventoryEntity cpi " +
            "WHERE cpi.cartId IN (SELECT c.id FROM CartEntity c WHERE c.user.id = :userId AND c.isPayment = true) AND " +
            "cpi.cartId IN (SELECT p.cartId FROM PaymentOrderEntity p WHERE p.paymentStatus = :paymentStatus AND p.deliveryStatus = :deliveryStatus)")
    List<CartProductInventoryEntity> findByUserIdAndPaidAndPaymentStatusAndDeliveryStatus(final UUID userId, final PaymentStatus paymentStatus, final DeliveryStatus deliveryStatus);
}
