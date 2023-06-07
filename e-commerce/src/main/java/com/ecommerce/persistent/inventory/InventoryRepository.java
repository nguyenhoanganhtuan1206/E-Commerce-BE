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

    @Query("select i.colorValue from InventoryEntity i where i.colorName = :colorName")
    List<String> findColorValueByColorName(final String colorName);

    @Query("select i.sizeValue from InventoryEntity i where i.sizeName = :sizeName")
    List<String> findSizeValueBySizeName(final String sizeName);
}
