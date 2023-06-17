package com.ecommerce.domain.cart.mapper;

import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.persistent.cart.CartEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryDTO;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTO;

@UtilityClass
public class CartDetailMapperDTO {

    private final ModelMapper modelMapper = new ModelMapper();

    public static CartDetailResponseDTO toCartDetailDTO(final CartEntity cartEntity) {
        final CartDetailResponseDTO cartDetailResponseDTO = modelMapper.map(cartEntity, CartDetailResponseDTO.class);

        if (cartEntity.getProduct() != null) {
            cartDetailResponseDTO.setProduct(toProductDTO(cartEntity.getProduct()));
            cartDetailResponseDTO.setSellerId(cartEntity.getProduct().getSeller().getId());
        }

        if (cartEntity.getInventory() != null) {
            cartDetailResponseDTO
                    .setSellerId(cartEntity.getInventory().getProduct().getSeller().getId());
            cartDetailResponseDTO.setProduct(toProductDTO(cartEntity.getInventory().getProduct())
                    .withInventory(toInventoryDTO(cartEntity.getInventory())));
        }

        return cartDetailResponseDTO;
    }

    public static List<CartDetailResponseDTO> toCartDetailDTOs(final List<CartEntity> carts) {
        return carts.stream()
                .map(CartDetailMapperDTO::toCartDetailDTO)
                .toList();
    }
}
