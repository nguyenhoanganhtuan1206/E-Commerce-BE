package com.ecommerce.persistent.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findBySellerId(final UUID sellerId);

    Optional<ProductEntity> findByNameContainingIgnoreCase(final String productName);

    @Query(value = "select p from ProductEntity p where p.name like %:searchTemp% or p.seller.sellerName like %:searchTemp%")
    List<ProductEntity> findByNameOrSellerName(final String searchTemp);
}
