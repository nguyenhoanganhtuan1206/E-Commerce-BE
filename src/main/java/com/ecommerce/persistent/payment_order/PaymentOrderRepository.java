package com.ecommerce.persistent.payment_order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrderEntity, UUID> {

    Optional<PaymentOrderEntity> findByCartId(UUID cartId);

    @Query("SELECT p FROM PaymentOrderEntity p WHERE p.cartId IN (SELECT c FROM CartEntity c WHERE c.sellerId = :sellerId) " +
            "ORDER BY p.orderedAt DESC")
    List<PaymentOrderEntity> findBySellerId(final UUID sellerId);
}
