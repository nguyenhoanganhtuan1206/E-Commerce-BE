package com.ecommerce.domain.cart_product_inventory;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryRepository;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.cart.CartError.supplyCartValidation;
import static com.ecommerce.domain.cart_product_inventory.CartProductInventoryError.supplyCartProductInventoryNotFound;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.error.CommonError.supplyNotFoundException;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class CartProductInventoryService {

    private final CartProductInventoryRepository cartProductInventoryRepository;

    private final CommonProductService commonProductService;

    private final CartService cartService;

    private final UserService userService;

    private final InventoryService inventoryService;

    private final AuthsProvider authProvider;

    public CartProductInventoryEntity save(final CartProductInventoryEntity cartProductInventory) {
        return cartProductInventoryRepository.save(cartProductInventory);
    }

    public CartProductInventoryEntity findByUserIdAndProductId(final UUID userId, final UUID productId) {
        return cartProductInventoryRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(supplyNotFoundException("This cart is not existed!"));
    }

    public CartProductInventoryEntity findByUserIdAndInventoryId(final UUID userId, final UUID productId) {
        return cartProductInventoryRepository.findByUserIdAndInventoryId(userId, productId)
                .orElseThrow(supplyNotFoundException("This cart is not existed!"));
    }

    public List<CartDetailResponseDTO> findCartByCurrentUser() {
        return toCartDetailResponseDTOs(cartProductInventoryRepository.findByUserId(authProvider.getCurrentUserId()));
    }

    public CartProductInventoryEntity findById(final UUID id) {
        return cartProductInventoryRepository.findById(id)
                .orElseThrow(supplyCartProductInventoryNotFound("id", id));
    }

    public List<CartDetailResponseDTO> findByUserIdAndSellerId(final UUID sellerId) {
        return toCartDetailResponseDTOs(cartProductInventoryRepository.findByUserIdAndSellerId(authProvider.getCurrentUserId(), sellerId));
    }

    public CartProductInventoryEntity addProductToCart(final CartRequestDTO cartRequestDTO) {
        if (cartRequestDTO.getInventoryId() == null && cartRequestDTO.getProductId() == null) {
            throw supplyCartValidation("Your cart is currently empty! Please select some products.").get();
        }

        final UserEntity currentUser = userService.findById(authProvider.getCurrentUserId());

        if (cartRequestDTO.getProductId() != null) {
            return updateCartIfProductIdAvailable(cartRequestDTO, currentUser);
        }

        return updateCartIfInventoryIdAvailable(cartRequestDTO, currentUser);
    }

    public void increaseQuantity(final UUID currentCartId) {
        final CartProductInventoryEntity currentCart = findById(currentCartId);
        validateCartQuantityLimits(currentCart);

        currentCart.setQuantity(currentCart.getQuantity() + 1);
        updatePriceWhenInteractWithQuantity(currentCart);

        cartProductInventoryRepository.save(currentCart);
    }

    public void decreaseQuantity(final UUID currentCartId) {
        final CartProductInventoryEntity currentCart = findById(currentCartId);

        if (currentCart.getQuantity() < 0) {
            throw supplyCartValidation("You have to select at least 1 unit.").get();
        }

        currentCart.setQuantity(currentCart.getQuantity() - 1);
        updatePriceWhenInteractWithQuantity(currentCart);

        cartProductInventoryRepository.save(currentCart);
    }

    private CartProductInventoryEntity updateCartIfProductIdAvailable(
            final CartRequestDTO cartRequestDTO,
            final UserEntity currentUser
    ) {
        final ProductEntity productSelected = commonProductService.findById(cartRequestDTO.getProductId());
        final Optional<CartProductInventoryEntity> currentCart = cartProductInventoryRepository.findByUserIdAndProductId(authProvider.getCurrentUserId(), productSelected.getId());

        if (cartRequestDTO.getQuantity() > productSelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        updateProductQuantity(productSelected, cartRequestDTO);
        if (currentCart.isEmpty()) {
            return createAndSaveNewCartProduct(currentUser, cartRequestDTO);
        }

        final int currentQuantity = currentCart.get().getQuantity() + cartRequestDTO.getQuantity();

        return cartProductInventoryRepository.save(
                currentCart.get()
                        .withTotalPrice(currentCart.get().getTotalPrice() + calculateTotalPrice(productSelected.getPrice(), cartRequestDTO))
                        .withQuantity(currentQuantity));
    }

    private CartProductInventoryEntity updateCartIfInventoryIdAvailable(
            final CartRequestDTO cartRequestDTO,
            final UserEntity currentUser
    ) {
        final InventoryEntity inventorySelected = inventoryService.findById(cartRequestDTO.getInventoryId());
        final Optional<CartProductInventoryEntity> currentCart = cartProductInventoryRepository.findByUserIdAndInventoryId(authProvider.getCurrentUserId(), inventorySelected.getId());

        if (cartRequestDTO.getQuantity() > inventorySelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }

        updateInventoryQuantity(inventorySelected, cartRequestDTO);
        if (currentCart.isEmpty()) {
            return createAndSaveNewCartProductInventory(cartRequestDTO, currentUser);
        }

        final int currentQuantity = currentCart.get().getQuantity() + cartRequestDTO.getQuantity();

        return cartProductInventoryRepository.save(
                currentCart.get()
                        .withTotalPrice(currentCart.get().getTotalPrice() + calculateTotalPrice(inventorySelected.getPrice(), cartRequestDTO))
                        .withQuantity(currentQuantity));
    }

    private CartProductInventoryEntity createAndSaveNewCartProduct(final UserEntity currentUser, final CartRequestDTO cartRequest) {
        final CartEntity newCart = createNewCart(currentUser);
        final long totalPrice = calculateTotalPrice(inventoryService.findById(cartRequest.getInventoryId()).getPrice(), cartRequest);

        return cartProductInventoryRepository.save(buildCartProduct(newCart, cartRequest, totalPrice));
    }

    private CartProductInventoryEntity createAndSaveNewCartProductInventory(final CartRequestDTO cartRequest, final UserEntity currentUser) {
        final CartEntity newCart = createNewCart(currentUser);
        final long totalPrice = calculateTotalPrice(inventoryService.findById(cartRequest.getInventoryId()).getPrice(), cartRequest);

        return cartProductInventoryRepository.save(buildCartProductInventory(cartRequest, newCart, totalPrice));
    }

    private CartProductInventoryEntity buildCartProduct(final CartEntity newCart, final CartRequestDTO cartRequest, final long totalPrice) {
        return CartProductInventoryEntity.builder()
                .cartId(newCart.getId())
                .productId(cartRequest.getProductId())
                .quantity(cartRequest.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    private CartProductInventoryEntity buildCartProductInventory(final CartRequestDTO cartRequest, final CartEntity cartCreated, final long totalPrice) {
        return CartProductInventoryEntity.builder()
                .cartId(cartCreated.getId())
                .inventoryId(cartRequest.getInventoryId())
                .quantity(cartRequest.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    private long calculateTotalPrice(final long price, final CartRequestDTO cartRequest) {
        return price * cartRequest.getQuantity();
    }

    private CartEntity createNewCart(final UserEntity currentUser) {
        return cartService.save(CartEntity.builder()
                .createdAt(now())
                .user(currentUser)
                .build());
    }

    private int calculateUpdatedQuantity(final CartProductInventoryEntity currentCart, final int quantitySelected, final CartRequestDTO cartRequest) {
        return currentCart.getQuantity() == quantitySelected
                ? quantitySelected
                : currentCart.getQuantity() + cartRequest.getQuantity();
    }

    private void validateCartQuantityLimits(final CartProductInventoryEntity currentCart) {
        if (currentCart.getProductId() != null) {
            final ProductEntity productSelected = commonProductService.findById(currentCart.getProductId());

            if (currentCart.getQuantity() > productSelected.getQuantity()) {
                throw supplyCartValidation("Can't select exceeds current quantity.").get();
            }
            return;
        }

        final InventoryEntity inventorySelected = inventoryService.findById(currentCart.getInventoryId());
        if (currentCart.getQuantity() > inventorySelected.getQuantity()) {
            throw supplyCartValidation("Can't select exceeds current quantity.").get();
        }
    }

    private void updatePriceWhenInteractWithQuantity(final CartProductInventoryEntity currentCart) {
        if (currentCart.getProductId() != null) {
            final ProductEntity productSelected = commonProductService.findById(currentCart.getProductId());

            currentCart.setTotalPrice(currentCart.getQuantity() * productSelected.getPrice());
        }

        if (currentCart.getInventoryId() != null) {
            final InventoryEntity inventorySelected = inventoryService.findById(currentCart.getInventoryId());

            currentCart.setTotalPrice(currentCart.getQuantity() * inventorySelected.getPrice());
        }
    }

    private void updateInventoryQuantity(final InventoryEntity inventorySelected, final CartRequestDTO cartRequest) {
        inventoryService.save(inventorySelected
                .withQuantity(inventorySelected.getQuantity() - cartRequest.getQuantity()));
    }

    private void updateProductQuantity(final ProductEntity productSelected, final CartRequestDTO cartRequest) {
        commonProductService.save(productSelected
                .withQuantity(productSelected.getQuantity() - cartRequest.getQuantity()));
    }

    private List<CartDetailResponseDTO> toCartDetailResponseDTOs(final List<CartProductInventoryEntity> carts) {
        return carts.stream()
                .map(this::toCartDetailResponseDTO)
                .toList();
    }

    private CartDetailResponseDTO toCartDetailResponseDTO(final CartProductInventoryEntity cartProductInventory) {
        final CartEntity currentCart = cartService.findById(cartProductInventory.getCartId());
        final CartDetailResponseDTO cartDetailResponseDTO = CartDetailResponseDTO.builder()
                .id(cartProductInventory.getCartId())
                .quantity(cartProductInventory.getQuantity())
                .totalPrice(cartProductInventory.getTotalPrice())
                .createdAt(currentCart.getCreatedAt())
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
}
