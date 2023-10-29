package com.ecommerce.persistent.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, UUID> {

    List<LocationEntity> findLocationEntitiesByUserId(final UUID userId);

    Optional<LocationEntity> findByDefaultLocationTrue();

    @Query("select l from LocationEntity l where l.user.id = :userId " +
            "order by l.defaultLocation desc, l.createdAt desc")
    List<LocationEntity> findLocationsByUserIdAndSorted(final UUID userId);
}
