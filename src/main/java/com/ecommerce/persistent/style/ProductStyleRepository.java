package com.ecommerce.persistent.style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductStyleRepository extends JpaRepository<ProductStyleEntity, UUID> {

    @Query("select ps from ProductStyleEntity ps where ps.seller is null")
    List<ProductStyleEntity> findAllWithoutSellerId();

    Optional<ProductStyleEntity> findByName(final String name);

    Optional<ProductStyleEntity> findByNameAndSellerId(final String name, final UUID sellerId);

    List<ProductStyleEntity> findBySellerId(final UUID sellerId);
}
