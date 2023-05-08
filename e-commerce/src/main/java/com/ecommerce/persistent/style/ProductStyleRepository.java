package com.ecommerce.persistent.style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductStyleRepository extends JpaRepository<ProductStyleEntity, UUID> {

    Optional<ProductStyleEntity> findByName(final String name);
}
