package com.ecommerce.domain.cart;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.cart_product_inventory.CartProductInventoryService;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart.CartRepository;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.cart.CartError.supplyCartNotFound;
import static com.ecommerce.domain.cart.CartError.supplyCartValidation;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartProductInventoryService cartProductInventoryService;

    private final CommonProductService commonProductService;

    private final InventoryService inventoryService;

    private final AuthsProvider authsProvider;

    public CartEntity findById(final UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(supplyCartNotFound(cartId, "id"));
    }

    @Transactional
    public void deleteById(final UUID cartProductInventoryId) {
        final CartProductInventoryEntity cartProductInventory = cartProductInventoryService.findById(cartProductInventoryId);
        final CartEntity currentCart = findById(cartProductInventory.getCartId());

        currentCart.setTotalPrice(currentCart.getTotalPrice() - cartProductInventory.getTotalPrice());
        cartRepository.save(currentCart);
        cartProductInventoryService.deleteById(cartProductInventoryId);
        deleteCartIfProductIsEmpty(currentCart.getId());
    }

    public List<CartDetailResponseDTO> findDetailsCart(final UUID sellerId) {
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndSellerId(authsProvider.getCurrentUserId(), sellerId);

        if (currentCart.isEmpty()) {
            return Collections.emptyList();
        }

        return cartProductInventoryService.toCartDetailResponseDTOs(cartProductInventoryService.findByCartId(currentCart.get().getId()));
    }

    public CartEntity save(final CartEntity cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public void increaseQuantity(final UUID cartProductInventoryId) {
        final CartProductInventoryEntity cartProductInventory = cartProductInventoryService.findById(cartProductInventoryId);
        final CartEntity currentCart = findById(cartProductInventory.getCartId());

        validateCartQuantityLimits(cartProductInventory);

        cartProductInventory.setQuantity(cartProductInventory.getQuantity() + 1);
        updatePriceWhenInteractWithQuantity(currentCart, cartProductInventory);

        cartProductInventoryService.save(cartProductInventory);
    }

    @Transactional
    public void decreaseQuantity(final UUID cartProductInventoryId) {
        final CartProductInventoryEntity cartProductInventory = cartProductInventoryService.findById(cartProductInventoryId);
        final CartEntity currentCart = findById(cartProductInventory.getCartId());

        if (cartProductInventory.getQuantity() < 0) {
            throw supplyCartValidation("You have to select at least 1 unit.").get();
        }

        cartProductInventory.setQuantity(cartProductInventory.getQuantity() - 1);
        updatePriceWhenInteractWithQuantity(currentCart, cartProductInventory);

        cartProductInventoryService.save(cartProductInventory);
    }

    private void deleteCartIfProductIsEmpty(final UUID cartId) {
        final List<CartProductInventoryEntity> cartProductInventoryEntities = cartProductInventoryService.findByCartId(cartId);

        if (cartProductInventoryEntities.isEmpty()) {
            cartRepository.deleteById(cartId);
        }
    }

    private void updatePriceWhenInteractWithQuantity(final CartEntity currentCart,
                                                     final CartProductInventoryEntity cartProductInventory) {
        if (cartProductInventory.getProductId() != null) {
            final ProductEntity productSelected = commonProductService.findById(cartProductInventory.getProductId());

            cartProductInventory.setTotalPrice(cartProductInventory.getQuantity() * productSelected.getPrice());
            currentCart.setTotalPrice(currentCart.getTotalPrice() + productSelected.getPrice());
        }

        if (cartProductInventory.getInventoryId() != null) {
            final InventoryEntity inventorySelected = inventoryService.findById(cartProductInventory.getInventoryId());

            cartProductInventory.setTotalPrice(cartProductInventory.getQuantity() * inventorySelected.getPrice());
            currentCart.setTotalPrice(currentCart.getTotalPrice() + inventorySelected.getPrice());
        }

        cartProductInventoryService.save(cartProductInventory);
        cartRepository.save(currentCart);
    }

    private void validateCartQuantityLimits(final CartProductInventoryEntity cartProductInventory) {
        if (cartProductInventory.getProductId() != null) {
            final ProductEntity productSelected = commonProductService.findById(cartProductInventory.getProductId());

            if (cartProductInventory.getQuantity() > productSelected.getQuantity()) {
                throw supplyCartValidation("Can't select exceeds current quantity.").get();
            }
        }

        final InventoryEntity inventorySelected = inventoryService.findById(cartProductInventory.getInventoryId());
        if (cartProductInventory.getQuantity() > inventorySelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }
    }
}
