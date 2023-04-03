package com.ecommerce.persistent.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, UUID> {

    Optional<SellerEntity> findByEmailSeller(final String emailSeller);
}
