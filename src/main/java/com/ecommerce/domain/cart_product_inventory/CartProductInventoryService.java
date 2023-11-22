package com.ecommerce.domain.cart_product_inventory;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
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
import static com.ecommerce.domain.cart_product_inventory.CartProductInventoryValidation.validateCartRequestNotEmpty;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
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

    /*
     TODO: ItemId which mean that it can be inventory or product
    * */
    public CartProductInventoryEntity findByCartIdAndItemId(final UUID currentCartId, final UUID itemId) {
        final List<CartProductInventoryEntity> cartProductInventories = findByCartId(currentCartId);

        return cartProductInventories.stream()
                .filter(cartProductInventory -> isCartMatching(cartProductInventory, itemId))
                .findFirst()
                .orElse(null);
    }

    private boolean isCartMatching(final CartProductInventoryEntity cartProductInventory, final UUID itemId) {
        if (cartProductInventory.getInventoryId() != null) {
            return cartProductInventoryRepository.findByCartIdAndInventoryId(cartProductInventory.getCartId(), itemId).isPresent();
        }

        return cartProductInventoryRepository.findByCartIdAndProductId(cartProductInventory.getCartId(), itemId).isPresent();
    }

    public CartProductInventoryEntity save(final CartProductInventoryEntity cartProductInventory) {
        return cartProductInventoryRepository.save(cartProductInventory);
    }

    public List<CartDetailResponseDTO> findCartByCurrentUser() {
        return toCartDetailResponseDTOs(cartProductInventoryRepository.findByUserId(authProvider.getCurrentUserId()));
    }

    public CartProductInventoryEntity findById(final UUID id) {
        return cartProductInventoryRepository.findById(id)
                .orElseThrow(supplyCartProductInventoryNotFound("id", id));
    }

    @Transactional
    public void addProductToCart(final CartRequestDTO cartRequestDTO) {
        validateCartRequestNotEmpty(cartRequestDTO);

        final UserEntity currentUser = userService.findById(authProvider.getCurrentUserId());

        if (cartRequestDTO.getProductId() != null) {
            updateCartIfProductIdAvailable(cartRequestDTO, currentUser);
        }

        if (cartRequestDTO.getInventoryId() != null) {
            updateCartIfInventoryIdAvailable(cartRequestDTO, currentUser);
        }
    }

    public CartDetailResponseDTO toCartDetailResponseDTO(final CartProductInventoryEntity cartProductInventory) {
        final UserEntity user = userService.findById(authProvider.getCurrentUserId());

        final CartDetailResponseDTO cartDetailResponseDTO = CartDetailResponseDTO.builder()
                .id(cartProductInventory.getCartId())
                .quantity(cartProductInventory.getQuantity())
                .user(toUserDTO(user))
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
            final Optional<CartProductInventoryEntity> cartProductInventory = cartProductInventoryRepository.findByCartIdAndProductId(currentCart.get().getId(), productSelected.getId());

            if (cartProductInventory.isPresent()) {
                updateExistingCartProduct(cartProductInventory.get(), cartRequestDTO, productSelected.getPrice(), currentCart.get());
            } else {
                cartProductInventoryRepository.save(buildCartProduct(currentCart.get(), cartRequestDTO));
            }

            cartRepository.save(currentCart.get()
                    .withTotalPrice(updateTotalPriceWhenCartExisting(currentCart.get(), productSelected.getPrice(), cartRequestDTO)));
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
            final Optional<CartProductInventoryEntity> cartProductInventory = cartProductInventoryRepository.findByCartIdAndInventoryId(currentCart.get().getId(), inventorySelected.getId());

            if (cartProductInventory.isPresent()) {
                updateExistingCartProduct(cartProductInventory.get(), cartRequestDTO, inventorySelected.getPrice(), currentCart.get());
            } else {
                cartProductInventoryRepository.save(buildCartProduct(currentCart.get(), cartRequestDTO));
            }

            cartRepository.save(currentCart.get()
                    .withTotalPrice(updateTotalPriceWhenCartExisting(currentCart.get(), inventorySelected.getPrice(), cartRequestDTO)));
        }
    }

    private long updateTotalPriceWhenCartExisting(final CartEntity currentCart, final long priceProductSelected, final CartRequestDTO cartRequestDTO) {
        return currentCart.getTotalPrice() + calculateTotalPrice(priceProductSelected, cartRequestDTO);
    }

    private void updateExistingCartProduct(final CartProductInventoryEntity cartProductInventory,
                                           final CartRequestDTO cartRequestDTO,
                                           final long priceItemSelected,
                                           final CartEntity currentCart) {
        final int currentQuantity = cartProductInventory.getQuantity() + cartRequestDTO.getQuantity();

        cartProductInventoryRepository.save(cartProductInventory
                .withQuantity(currentQuantity));

        cartRepository.save(currentCart
                .withTotalPrice(currentQuantity * priceItemSelected));
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
