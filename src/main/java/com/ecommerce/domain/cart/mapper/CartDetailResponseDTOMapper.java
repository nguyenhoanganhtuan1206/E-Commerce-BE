package com.ecommerce.domain.cart.mapper;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;

@RequiredArgsConstructor
public class CartDetailResponseDTOMapper {

    private final CartService cartService;

    private final InventoryService inventoryService;

    private final CommonProductService commonProductService;

    public CartDetailResponseDTO toCartDetailResponseDTO(final CartProductInventoryEntity cartProductInventory) {
        final CartEntity currentCart = cartService.findById(cartProductInventory.getCartId());

        final CartDetailResponseDTO cartDetailResponseDTO = CartDetailResponseDTO.builder()
                .id(cartProductInventory.getCartId())
                .quantity(cartProductInventory.getQuantity())
                .createdAt(currentCart.getCreatedAt())
                .user(toUserDTO(currentCart.getUser()))
                .build();

        if (cartProductInventory.getInventoryId() != null) {
            final InventoryEntity currentInventory = inventoryService.findById(cartProductInventory.getInventoryId());

            cartDetailResponseDTO.setProduct(toProductDTO(currentInventory.getProduct()));
            cartDetailResponseDTO.setSeller(toSellerDTO(currentInventory.getProduct().getSeller()));
        }

        if (cartProductInventory.getProductId() != null) {
            final ProductEntity currentProduct = commonProductService.findById(cartProductInventory.getProductId());

            cartDetailResponseDTO.setProduct(toProductDTO(currentProduct));
            cartDetailResponseDTO.setSeller(toSellerDTO(currentProduct.getSeller()));
        }

        return cartDetailResponseDTO;
    }

    public List<CartDetailResponseDTO> toCartDetailResponseDTOs(final List<CartProductInventoryEntity> carts) {
        return carts.stream()
                .map(this::toCartDetailResponseDTO)
                .toList();
    }
}
