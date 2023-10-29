package com.ecommerce.domain.cart.mapper;

import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.persistent.cart.CartEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryDTO;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;

@UtilityClass
public class CartDetailMapperDTO {

    private final ModelMapper modelMapper = new ModelMapper();

    public static CartDetailResponseDTO toCartDetailDTO(final CartEntity cartEntity) {
        final CartDetailResponseDTO cartDetailResponseDTO = modelMapper.map(cartEntity, CartDetailResponseDTO.class);

        if (cartEntity.getProduct() != null) {
            cartDetailResponseDTO.setProduct(toProductDTO(cartEntity.getProduct()));
            cartDetailResponseDTO.setSeller(toSellerDTO(cartEntity.getProduct().getSeller()));
        }

        if (cartEntity.getInventory() != null) {
            cartDetailResponseDTO
                    .setSeller(toSellerDTO(cartEntity.getInventory().getProduct().getSeller()));
            cartDetailResponseDTO.setProduct(toProductDTO(cartEntity.getInventory().getProduct())
                    .withInventory(toInventoryDTO(cartEntity.getInventory())));
        }

        cartDetailResponseDTO.setUser(toUserDTO(cartEntity.getUser()));

        return cartDetailResponseDTO;
    }

    public static List<CartDetailResponseDTO> toCartDetailDTOs(final List<CartEntity> carts) {
        return carts.stream()
                .map(CartDetailMapperDTO::toCartDetailDTO)
                .toList();
    }
}
