package com.ecommerce.persistent.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, UUID> {

    Set<LocationEntity> findLocationEntitiesByUserId(final UUID userId);

    Optional<LocationEntity> findLocationEntitiesByDefaultLocationTrue();
}
