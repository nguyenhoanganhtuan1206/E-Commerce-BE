package com.ecommerce.domain.inventory.mapper;

import com.ecommerce.domain.inventory.dto.InventoryUpdateRequestDTO;
import com.ecommerce.persistent.inventory.InventoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class InventoryUpdateDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static InventoryEntity toInventoryEntity(final InventoryUpdateRequestDTO inventoryDTO) {
        return modelMapper.map(inventoryDTO, InventoryEntity.class);
    }

    public static List<InventoryEntity> toInventoryEntities(final List<InventoryUpdateRequestDTO> inventories) {
        return inventories.stream()
                .map(InventoryUpdateDTOMapper::toInventoryEntity)
                .toList();
    }
}
