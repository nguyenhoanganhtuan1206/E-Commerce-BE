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
import com.ecommerce.persistent.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.cart.CartError.supplyCartNotFound;
import static com.ecommerce.domain.cart.CartError.supplyCartValidation;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CommonProductService commonProductService;

    private final InventoryService inventoryService;

    private final UserService userService;

    private final AuthsProvider authProvider;

    public List<CartEntity> findCartByCurrentUserId() {
        return cartRepository.findByUserId(authProvider.getCurrentUserId());
    }

    public CartEntity findById(final UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(supplyCartNotFound(cartId, "id"));
    }

    public void addProductToCart(final CartRequestDTO cartRequestDTO) {
        if (cartRequestDTO.getInventoryId() == null && cartRequestDTO.getProductId() == null) {
            throw supplyCartValidation("Your cart is currently empty! Please select some products.").get();
        }

        final UserEntity currentUser = userService.findById(authProvider.getCurrentUserId());

        if (cartRequestDTO.getProductId() != null) {
            cartRepository.save(updateCartIfProductIdAvailable(cartRequestDTO, currentUser));
        }

        if (cartRequestDTO.getInventoryId() != null) {
            cartRepository.save(updateCartIfInventoryIdAvailable(cartRequestDTO, currentUser));
        }
    }

    public void increaseQuantity(final UUID cartId) {
        final CartEntity currentCart = findById(cartId);
        validateCartQuantityLimits(currentCart);

        currentCart.setQuantity(currentCart.getQuantity() + 1);
        updatePriceWhenInteractWithQuantity(currentCart);

        cartRepository.save(currentCart);
    }

    public void decreaseQuantity(final UUID cartId) {
        final CartEntity currentCart = findById(cartId);

        if (currentCart.getQuantity() < 0) {
            throw supplyCartValidation("Something went wrong, please try again.").get();
        }

        currentCart.setQuantity(currentCart.getQuantity() - 1);
        updatePriceWhenInteractWithQuantity(currentCart);
        
        cartRepository.save(currentCart);
    }

    public void deleteById(final UUID cartId) {
        final CartEntity cartEntity = findById(cartId);

        cartRepository.delete(cartEntity);
    }

    public CartEntity updateCartIfProductIdAvailable(
            final CartRequestDTO cartRequestDTO,
            final UserEntity currentUser
    ) {
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndProductId(authProvider.getCurrentUserId(), cartRequestDTO.getProductId());
        final ProductEntity productSelected = commonProductService.findById(cartRequestDTO.getProductId());
        final double totalPrice = productSelected.getPrice() * cartRequestDTO.getQuantity();

        if (cartRequestDTO.getQuantity() > productSelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        if (currentCart.isEmpty()) {
            return CartEntity.builder()
                    .createdAt(Instant.now())
                    .product(productSelected)
                    .quantity(cartRequestDTO.getQuantity())
                    .totalPrice(totalPrice)
                    .user(currentUser)
                    .build();
        }

        return currentCart.get()
                .withTotalPrice(currentCart.get().getTotalPrice() + totalPrice)
                .withQuantity(currentCart.get().getQuantity() + cartRequestDTO.getQuantity());
    }

    public CartEntity updateCartIfInventoryIdAvailable(
            final CartRequestDTO cartRequestDTO,
            final UserEntity currentUser
    ) {
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndInventoryId(authProvider.getCurrentUserId(), cartRequestDTO.getInventoryId());
        final InventoryEntity inventorySelected = inventoryService.findById(cartRequestDTO.getInventoryId());
        final double totalPrice = inventorySelected.getPrice() * cartRequestDTO.getQuantity();

        if (cartRequestDTO.getQuantity() > inventorySelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        if (currentCart.isEmpty()) {
            return CartEntity.builder()
                    .createdAt(Instant.now())
                    .inventory(inventorySelected)
                    .quantity(cartRequestDTO.getQuantity())
                    .totalPrice(totalPrice)
                    .user(currentUser)
                    .build();
        }

        return currentCart.get()
                .withTotalPrice(currentCart.get().getTotalPrice() + totalPrice)
                .withQuantity(currentCart.get().getQuantity() + cartRequestDTO.getQuantity());
    }

    private void updatePriceWhenInteractWithQuantity(final CartEntity currentCart) {
        if (currentCart.getProduct() != null) {
            currentCart.setTotalPrice(currentCart.getQuantity() * currentCart.getProduct().getPrice());
        }

        if (currentCart.getInventory() != null) {
            currentCart.setTotalPrice(currentCart.getQuantity() * currentCart.getInventory().getPrice());
        }
    }

    private void validateCartQuantityLimits(final CartEntity currentCart) {
        if (currentCart.getProduct() != null && currentCart.getQuantity() > currentCart.getProduct().getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        if (currentCart.getQuantity() > currentCart.getInventory().getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }
    }
}
