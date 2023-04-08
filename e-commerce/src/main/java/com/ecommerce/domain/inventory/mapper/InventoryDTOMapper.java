package com.ecommerce.domain.inventory.mapper;

import com.ecommerce.domain.inventory.InventoryDTO;
import com.ecommerce.persistent.inventory.InventoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class InventoryDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static InventoryDTO toInventoryDTO(final InventoryEntity inventoryEntity) {
        return modelMapper.map(inventoryEntity, InventoryDTO.class);
    }

    public static InventoryEntity toInventoryEntity(final InventoryDTO inventoryDTO) {
        return modelMapper.map(inventoryDTO, InventoryEntity.class);
    }
}
