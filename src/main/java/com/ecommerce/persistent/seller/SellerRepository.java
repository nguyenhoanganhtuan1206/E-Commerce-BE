package com.ecommerce.persistent.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, UUID> {

    @Query("select s from SellerEntity s order by s.createdAt DESC ")
    List<SellerEntity> findAllSortedByCreatedAt();

    Optional<SellerEntity> findByEmailSeller(final String emailSeller);

    Optional<SellerEntity> findByUserId(final UUID userId);
}
