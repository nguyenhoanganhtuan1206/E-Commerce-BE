package com.ecommerce.persistent.variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryVariantRepository extends JpaRepository<CategoryVariantEntity, UUID> {

    Optional<CategoryVariantEntity> findByVariantName(final String variantName);
}
