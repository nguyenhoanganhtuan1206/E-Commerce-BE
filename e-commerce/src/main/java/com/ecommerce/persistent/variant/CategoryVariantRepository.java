package com.ecommerce.persistent.variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryVariantRepository extends JpaRepository<CategoryVariantEntity, UUID> {

    @Query("select c from CategoryEntity c where c.categoryName = :categoryName")
    Set<CategoryVariantEntity> findByCategoryName(final String categoryName);

    Optional<CategoryVariantEntity> findByVariantName(final String variantName);
}
