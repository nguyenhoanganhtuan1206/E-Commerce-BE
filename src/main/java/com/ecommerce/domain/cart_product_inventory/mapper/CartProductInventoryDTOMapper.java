package com.ecommerce.domain.cart_product_inventory.mapper;

import com.ecommerce.domain.cart_product_inventory.dto.CartProductInventoryDTO;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class CartProductInventoryDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CartProductInventoryDTO toCartProductInventoryDTO(final CartProductInventoryEntity cartProductInventory) {
        return modelMapper.map(cartProductInventory, CartProductInventoryDTO.class);
    }

    public static List<CartProductInventoryDTO> toCartProductInventoryDTOs(final List<CartProductInventoryEntity> cartProductInventoryEntities) {
        return cartProductInventoryEntities.stream()
                .map(CartProductInventoryDTOMapper::toCartProductInventoryDTO)
                .toList();
    }
}
