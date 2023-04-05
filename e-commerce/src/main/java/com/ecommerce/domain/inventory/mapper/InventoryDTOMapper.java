package com.ecommerce.domain.inventory.mapper;

import com.ecommerce.domain.inventory.InventoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class InventoryDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static InventoryDTO toInventoryDTO(final InventoryEntity inventoryEntity) {
        final InventoryDTO inventory = modelMapper.map(inventoryEntity, InventoryDTO.class);

        inventory.setProduct(modelMapper.map(inventoryEntity.getProduct(), ProductDTO.class));
        return inventory;
    }

    public static InventoryEntity toInventoryEntity(final InventoryDTO inventoryDTO) {
        final InventoryEntity inventory = modelMapper.map(inventoryDTO, InventoryEntity.class);

        inventory.setProduct(modelMapper.map(inventoryDTO.getProduct(), ProductEntity.class));
        return inventory;
    }
}
