package com.ecommerce.domain.inventory;

import com.ecommerce.persistent.inventory.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryDTO;
import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryEntity;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryDTO save(final InventoryDTO inventoryDTO) {
        inventoryDTO.setCreatedAt(Instant.now());

        return toInventoryDTO(inventoryRepository.save(toInventoryEntity(inventoryDTO)));
    }
}
