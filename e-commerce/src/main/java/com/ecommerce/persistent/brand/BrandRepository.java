package com.ecommerce.persistent.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {

    Optional<BrandEntity> findByBrandName(final String name);

    @Query("select b from BrandEntity b where b.category.categoryName = :categoryName")
    Set<BrandEntity> findByCategoryName(final String categoryName);
}
