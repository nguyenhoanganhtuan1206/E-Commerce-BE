package com.ecommerce.persistent.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    List<InventoryEntity> findByProductId(final UUID productId);

    @Query("select distinct i.colorValue from InventoryEntity i where i.product.id = :productId")
    List<String> findColorValueByProductId(final UUID productId);

    @Query("select distinct i.sizeValue from InventoryEntity i where i.product.id = :productId")
    List<String> findSizeValueByProductId(final UUID productId);

    @Query("select i.sizeValue from InventoryEntity i where i.colorValue = :colorValue and i.product.id = :productId")
    List<String> findSizeValuesByColorValueAndProductId(final String colorValue, final UUID productId);

    @Query("select i.colorValue from InventoryEntity i where i.sizeValue = :sizeValue and i.product.id = :productId")
    List<String> findColorValuesBySizeValueAndProductId(final String sizeValue, final UUID productId);

    InventoryEntity findByColorValueAndSizeValueAndProductId(final String colorValue, final String sizeValue, final UUID productId);
}
