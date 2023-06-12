package com.ecommerce.domain.cart.mapper;

import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.persistent.cart.CartEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class CartDetailMapperDTO {

    private final ModelMapper modelMapper = new ModelMapper();

    public static CartDetailResponseDTO toCartDetailDTO(final CartEntity cartEntity) {
        return modelMapper.map(cartEntity, CartDetailResponseDTO.class);
    }

    public static List<CartDetailResponseDTO> toCartDetailDTOs(final List<CartEntity> carts) {
        return carts.stream()
                .map(CartDetailMapperDTO::toCartDetailDTO)
                .toList();
    }
}
