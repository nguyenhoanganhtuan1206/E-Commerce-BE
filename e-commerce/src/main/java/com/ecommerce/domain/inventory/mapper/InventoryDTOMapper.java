package com.ecommerce.domain.inventory.mapper;

import com.ecommerce.api.inventory.InventoryCreateRequestDTO;
import com.ecommerce.api.inventory.InventoryResponseDTO;
import com.ecommerce.persistent.inventory.InventoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class InventoryDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static InventoryCreateRequestDTO toInventoryRequestDTO(final InventoryEntity inventory) {
        return modelMapper.map(inventory, InventoryCreateRequestDTO.class);
    }

    public static InventoryEntity toInventoryEntity(final InventoryCreateRequestDTO inventoryDTO) {
        return modelMapper.map(inventoryDTO, InventoryEntity.class);
    }

    public static List<InventoryEntity> toInventoryEntities(final List<InventoryCreateRequestDTO> inventories) {
        return inventories.stream()
                .map(InventoryDTOMapper::toInventoryEntity)
                .toList();
    }

    public static InventoryResponseDTO toInventoryResponseDTO(final InventoryEntity inventory) {
        final InventoryResponseDTO inventoryResponseDTO = modelMapper.map(inventory, InventoryResponseDTO.class);

        inventoryResponseDTO.setProductId(inventory.getProduct().getId());

        return inventoryResponseDTO;
    }
}
