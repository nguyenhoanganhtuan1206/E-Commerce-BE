package com.ecommerce.domain.inventory;

import com.ecommerce.domain.inventory.dto.InventoryDetailResponseDTO;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.inventory.InventoryRepository;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.inventory.InventoryError.supplyInventoryNotFound;
import static com.ecommerce.domain.inventory.InventoryValidation.validateInventoryInformationNotEmpty;
import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryRequestDTO;
import static com.ecommerce.domain.product.ProductError.supplyProductNotFound;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    public InventoryEntity save(final InventoryEntity inventory) {
        return inventoryRepository.save(inventory);
    }

    public InventoryEntity findById(final UUID inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(supplyInventoryNotFound("id", inventoryId));
    }

    public InventoryEntity findByColorValueAndSizeValueAndProductId(final UUID productId, final String sizeValue, final String colorValue) {
        return inventoryRepository.findByColorValueAndSizeValueAndProductId(colorValue, sizeValue, productId);
    }

    public List<String> findSizeValuesByColorValueAndProductId(final String colorValue, final UUID productId) {
        return inventoryRepository.findSizeValuesByColorValueAndProductId(colorValue, productId);
    }

    public List<String> findColorValuesBySizeValueAndProductId(final String sizeValue, final UUID productId) {
        return inventoryRepository.findColorValuesBySizeValueAndProductId(sizeValue, productId);
    }


    public List<InventoryEntity> findInventoriesByProductId(final UUID productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public InventoryDetailResponseDTO findInventoryDetailByProductId(final UUID productId) {
        final List<InventoryEntity> inventoryEntities = findInventoriesByProductId(productId);

        if (!inventoryEntities.isEmpty()) {
            final int quantityProducts = inventoryEntities.stream().mapToInt(InventoryEntity::getQuantity).sum();

            return InventoryDetailResponseDTO
                    .builder()
                    .colorName(inventoryEntities.get(0).getColorName())
                    .sizeName(inventoryEntities.get(0).getSizeName())
                    .price(inventoryEntities.get(0).getPrice())
                    .quantity(quantityProducts)
                    .colorValues(inventoryRepository.findColorValueByProductId(productId))
                    .sizeValues(inventoryRepository.findSizeValueByProductId(productId))
                    .build();
        }
        return null;
    }

    public void createInventories(final List<InventoryEntity> inventories, final ProductEntity product) {
        inventories.forEach(inventory -> updateInventoryWithInformation(inventory, product));
    }

    public void updateInventoryWithProductId(final UUID productId, final List<InventoryEntity> inventoriesUpdate) {
        final List<InventoryEntity> currentInventories = findInventoriesByProductId(productId);
        final ProductEntity product = productRepository.findById(productId)
                .orElseThrow(supplyProductNotFound("id", productId));

        currentInventories.forEach(currentInventory ->
                inventoriesUpdate.forEach(inventoryUpdate -> validateAndSaveInventory(currentInventory, inventoryUpdate, product))
        );
    }

    private void updateInventoryWithInformation(final InventoryEntity inventory, final ProductEntity product) {
        validateInventoryInformationNotEmpty(toInventoryRequestDTO(inventory));

        inventory.setColorName(inventory.getColorName());
        inventory.setColorValue(inventory.getColorValue());
        inventory.setPrice(inventory.getPrice());
        inventory.setSizeName(inventory.getSizeName());
        inventory.setSizeValue(inventory.getSizeValue());
        inventory.setProduct(product);

        inventoryRepository.save(inventory);
    }

    private void validateAndSaveInventory(final InventoryEntity currentInventory, final InventoryEntity inventoryUpdate, final ProductEntity product) {
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
    }
}
