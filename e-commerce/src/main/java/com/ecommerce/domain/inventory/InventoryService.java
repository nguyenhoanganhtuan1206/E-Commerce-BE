package com.ecommerce.domain.inventory;

import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.inventory.InventoryRepository;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.ecommerce.domain.inventory.InventoryValidation.validateInventoryInformationNotEmpty;
import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryRequestDTO;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

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
}
