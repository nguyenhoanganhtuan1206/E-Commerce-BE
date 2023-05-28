package com.ecommerce.domain.inventory;

import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.inventory.InventoryRepository;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.inventory.InventoryValidation.validateInventoryInformationNotEmpty;
import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryRequestDTO;
import static com.ecommerce.domain.product.ProductError.supplyProductNotFound;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    public List<InventoryEntity> findInventoriesByProductId(final UUID productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public void createInventories(final List<InventoryEntity> inventories, final ProductEntity product) {
        inventories
                .forEach(inventory -> {
                    validateInventoryInformationNotEmpty(toInventoryRequestDTO(inventory));

                    inventory.setColorName(inventory.getColorName());
                    inventory.setColorValue(inventory.getColorValue());
                    inventory.setPrice(inventory.getPrice());
                    inventory.setSizeName(inventory.getSizeName());
                    inventory.setSizeValue(inventory.getSizeValue());
                    inventory.setCreatedAt(Instant.now());
                    inventory.setProduct(product);

                    inventoryRepository.save(inventory);
                });
    }

    public void updateInventoryWithProductId(final UUID productId, final List<InventoryEntity> inventoriesUpdate) {
        final List<InventoryEntity> currentInventories = findInventoriesByProductId(productId);
        final ProductEntity product = productRepository.findById(productId)
                .orElseThrow(supplyProductNotFound("id", productId));

        currentInventories.forEach(currentInventory -> {
            inventoriesUpdate.forEach(inventoryUpdate -> {
                if (inventoryUpdate.getId() == null) {
                    inventoryUpdate.setProduct(product);
                    inventoryRepository.save(inventoryUpdate);
                }

                if (StringUtils.equals(currentInventory.getId().toString(), inventoryUpdate.getId().toString())) {
                    validateInventoryInformationNotEmpty(toInventoryRequestDTO(inventoryUpdate));

                    currentInventory.setColorName(inventoryUpdate.getColorName());
                    currentInventory.setColorValue(inventoryUpdate.getColorValue());
                    currentInventory.setSizeName(inventoryUpdate.getSizeName());
                    currentInventory.setSizeValue(inventoryUpdate.getSizeValue());
                    currentInventory.setQuantity(inventoryUpdate.getQuantity());
                    currentInventory.setPrice(inventoryUpdate.getPrice());

                    inventoryRepository.save(currentInventory);
                }
            });
        });
    }
}
