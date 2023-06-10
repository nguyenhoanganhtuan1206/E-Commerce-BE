package com.ecommerce.domain.cart;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart.CartRepository;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static com.ecommerce.domain.cart.CartError.supplyCartEmpty;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CommonProductService commonProductService;

    private final InventoryService inventoryService;

    private final UserService userService;

    private final AuthsProvider authProvider;

    public CartEntity addProductToCart(final CartRequestDTO cartRequestDTO) {
        if (cartRequestDTO.getInventoryId() == null && cartRequestDTO.getProductId() == null) {
            throw supplyCartEmpty().get();
        }

        if (cartRequestDTO.getProductId() != null) {
            return cartRepository.save(updateCartIfProductIdAvailable(cartRequestDTO));
        }

        return cartRepository.save(updateCartIfInventoryIdAvailable(cartRequestDTO));
    }

    private CartEntity updateCartIfProductIdAvailable(final CartRequestDTO cartRequestDTO) {
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndProductId(authProvider.getCurrentUserId(), cartRequestDTO.getProductId());
        final ProductEntity productSelected = commonProductService.findById(cartRequestDTO.getProductId());

        CartEntity cartEntity;
        if (currentCart.isPresent()) {
            cartEntity = currentCart.get();
            cartEntity.setQuantity(cartEntity.getQuantity() + 1);
            cartEntity.setTotalPrice(productSelected.getPrice() * cartEntity.getQuantity());
        } else {
            cartEntity = CartEntity.builder()
                    .createdAt(Instant.now())
                    .product(productSelected)
                    .quantity(1)
                    .totalPrice(productSelected.getPrice())
                    .user(userService.findById(authProvider.getCurrentUserId()))
                    .build();
        }

        return cartEntity;
    }

    private CartEntity updateCartIfInventoryIdAvailable(final CartRequestDTO cartRequestDTO) {
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndInventoryId(authProvider.getCurrentUserId(), cartRequestDTO.getInventoryId());
        final InventoryEntity inventorySelected = inventoryService.findById(cartRequestDTO.getInventoryId());

        CartEntity cartEntity;
        if (currentCart.isPresent()) {
            cartEntity = currentCart.get();
            cartEntity.setQuantity(cartEntity.getQuantity() + 1);
            cartEntity.setTotalPrice(inventorySelected.getPrice() * cartEntity.getQuantity());
        } else {
            cartEntity = CartEntity.builder()
                    .createdAt(Instant.now())
                    .inventory(inventorySelected)
                    .quantity(1)
                    .totalPrice(inventorySelected.getPrice())
                    .user(userService.findById(authProvider.getCurrentUserId()))
                    .build();
        }

        return cartEntity;
    }
}
