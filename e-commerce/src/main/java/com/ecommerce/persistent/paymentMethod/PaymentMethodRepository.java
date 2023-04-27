package com.ecommerce.persistent.paymentMethod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, UUID> {

    Optional<PaymentMethodEntity> findByNameIgnoreCase(final String name);
}
