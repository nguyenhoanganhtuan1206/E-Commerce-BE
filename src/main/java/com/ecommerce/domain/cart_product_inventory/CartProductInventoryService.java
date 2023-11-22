package com.ecommerce.domain.cart_product_inventory;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart.CartRepository;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryRepository;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.cart.CartError.supplyCartValidation;
import static com.ecommerce.domain.cart_product_inventory.CartProductInventoryError.supplyCartProductInventoryNotFound;
import static com.ecommerce.error.CommonError.supplyNotFoundException;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class CartProductInventoryService {

    private final CartProductInventoryRepository cartProductInventoryRepository;

    private final CartRepository cartRepository;

    private final CommonProductService commonProductService;

    private final UserService userService;

    private final InventoryService inventoryService;

    private final AuthsProvider authProvider;

    public List<CartProductInventoryEntity> findByCartId(final UUID cartId) {
        return cartProductInventoryRepository.findByCartId(cartId);
    }

    public CartProductInventoryEntity findByCartIdAndProductId(final UUID currentCartId, final UUID productId) {
        return cartProductInventoryRepository.findByCartIdAndProductId(currentCartId, productId)
                .orElseThrow(supplyNotFoundException("This cart is not existed!"));
    }

    public CartProductInventoryEntity findByCartIdAndInventoryIdOrProductId(final UUID currentCartId, final UUID itemId) {
        return cartProductInventoryRepository.findByCartIdAndInventoryIdOrProductId(currentCartId, itemId)
                .orElseThrow(supplyNotFoundException("This cart is not existed!"));
    }

    public CartProductInventoryEntity findByCartIdAndInventoryId(final UUID currentCartId, final UUID inventoryId) {
        return cartProductInventoryRepository.findByCartIdAndInventoryId(currentCartId, inventoryId)
                .orElseThrow(supplyNotFoundException("This cart is not existed!"));
    }

    public CartProductInventoryEntity save(final CartProductInventoryEntity cartProductInventory) {
        return cartProductInventoryRepository.save(cartProductInventory);
    }

//    public List<CartDetailResponseDTO> findCartByCurrentUser() {
//        final List<CartEntity> carts = cartRepository.findByUserId(authProvider.getCurrentUserId());
//        final Map<UUID, List<CartProductInventoryEntity>> currentCart = new HashMap<>();
//
//        for (final CartEntity cart : carts) {
//            currentCart.putIfAbsent(cart.getId(), cartProductInventoryRepository.findByCartId(cart.getId()));
//        }
//
//        return new ArrayList<>();
//    }

    public CartProductInventoryEntity findById(final UUID id) {
        return cartProductInventoryRepository.findById(id)
                .orElseThrow(supplyCartProductInventoryNotFound("id", id));
    }

    @Transactional
    public void addProductToCart(final CartRequestDTO cartRequestDTO) {
        if (cartRequestDTO.getInventoryId() == null && cartRequestDTO.getProductId() == null) {
            throw supplyCartValidation("Your cart is currently empty! Please select some products.").get();
        }

        final UserEntity currentUser = userService.findById(authProvider.getCurrentUserId());

        if (cartRequestDTO.getProductId() != null) {
            updateCartIfProductIdAvailable(cartRequestDTO, currentUser);
        }

        updateCartIfInventoryIdAvailable(cartRequestDTO, currentUser);
    }

    private void updateCartIfProductIdAvailable(
            final CartRequestDTO cartRequestDTO,
            final UserEntity currentUser
    ) {
        final ProductEntity productSelected = commonProductService.findById(cartRequestDTO.getProductId());
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndSellerId(authProvider.getCurrentUserId(), cartRequestDTO.getSellerId());

        if (cartRequestDTO.getQuantity() > productSelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        updateProductQuantity(productSelected, cartRequestDTO);
        if (currentCart.isEmpty()) {
            createAndSaveNewCartProduct(currentUser, cartRequestDTO, productSelected);
        } else {
            final CartProductInventoryEntity cartProductInventory = findByCartIdAndProductId(currentCart.get().getId(), productSelected.getId());
            final int currentQuantity = cartProductInventory.getQuantity() + cartRequestDTO.getQuantity();

            cartProductInventoryRepository.save(cartProductInventory
                    .withQuantity(currentQuantity));

            cartRepository.save(currentCart.get()
                    .withTotalPrice(currentQuantity * productSelected.getPrice()));
        }
    }

    private void updateCartIfInventoryIdAvailable(
            final CartRequestDTO cartRequestDTO,
            final UserEntity currentUser
    ) {
        final InventoryEntity inventorySelected = inventoryService.findById(cartRequestDTO.getInventoryId());
        final Optional<CartEntity> currentCart = cartRepository.findByUserIdAndSellerId(authProvider.getCurrentUserId(), cartRequestDTO.getSellerId());

        if (cartRequestDTO.getQuantity() > inventorySelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        updateInventoryQuantity(inventorySelected, cartRequestDTO);
        if (currentCart.isEmpty()) {
            createAndSaveNewCartProductInventory(currentUser, cartRequestDTO, inventorySelected);
        } else {
            final CartProductInventoryEntity cartProductInventory = findByCartIdAndInventoryId(currentCart.get().getId(), inventorySelected.getId());
            final int currentQuantity = cartProductInventory.getQuantity() + cartRequestDTO.getQuantity();

            cartProductInventoryRepository.save(cartProductInventory
                    .withQuantity(currentQuantity));

            cartRepository.save(currentCart.get()
                    .withTotalPrice(currentQuantity * inventorySelected.getPrice()));
        }
    }

    private void createAndSaveNewCartProduct(final UserEntity currentUser,
                                             final CartRequestDTO cartRequest,
                                             final ProductEntity productSelected) {
        final CartEntity newCart = createNewCart(currentUser, productSelected.getPrice(), cartRequest);

        cartProductInventoryRepository.save(buildCartProduct(newCart, cartRequest));
    }

    private void createAndSaveNewCartProductInventory(final UserEntity currentUser,
                                                      final CartRequestDTO cartRequest,
                                                      final InventoryEntity inventorySelected) {
        final CartEntity newCart = createNewCart(currentUser, inventorySelected.getPrice(), cartRequest);

        cartProductInventoryRepository.save(buildCartProductInventory(cartRequest, newCart));
    }

    private CartProductInventoryEntity buildCartProduct(final CartEntity newCart, final CartRequestDTO cartRequest) {
        return CartProductInventoryEntity.builder()
                .quantity(cartRequest.getQuantity())
                .cartId(newCart.getId())
                .productId(cartRequest.getProductId())
                .build();
    }

    private CartProductInventoryEntity buildCartProductInventory(final CartRequestDTO cartRequest, final CartEntity cartCreated) {
        return CartProductInventoryEntity.builder()
                .cartId(cartCreated.getId())
                .inventoryId(cartRequest.getInventoryId())
                .quantity(cartRequest.getQuantity())
                .build();
    }

    private long calculateTotalPrice(final long price, final CartRequestDTO cartRequest) {
        return price * cartRequest.getQuantity();
    }

    private CartEntity createNewCart(final UserEntity currentUser, final long price, final CartRequestDTO cartRequestDTO) {
        return cartRepository.save(CartEntity.builder()
                .totalPrice(calculateTotalPrice(price, cartRequestDTO))
                .isPayment(Boolean.FALSE)
                .createdAt(now())
                .user(currentUser)
                .sellerId(cartRequestDTO.getSellerId())
                .build());
    }

    private void updateInventoryQuantity(final InventoryEntity inventorySelected, final CartRequestDTO cartRequest) {
        inventoryService.save(inventorySelected
                .withQuantity(inventorySelected.getQuantity() - cartRequest.getQuantity()));
    }

    private void updateProductQuantity(final ProductEntity productSelected, final CartRequestDTO cartRequest) {
        commonProductService.save(productSelected
                .withQuantity(productSelected.getQuantity() - cartRequest.getQuantity()));
    }
}
