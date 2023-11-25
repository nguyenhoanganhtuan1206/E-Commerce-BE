package com.ecommerce.api.cart;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.cart_product_inventory.CartProductInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/carts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','SELLER')")
public class CartController {

    private final CartService cartService;

    private final CartProductInventoryService cartProductInventoryService;

    @GetMapping("details")
    public List<CartDetailResponseDTO> findCartByCurrentUser() {
        return cartProductInventoryService.findCartByCurrentUser();
    }

    @GetMapping("{sellerId}/cart-details")
    public List<CartDetailResponseDTO> findByUserIdAndSellerId(@PathVariable final UUID sellerId) {
        return cartService.findDetailsCart(sellerId);
    }

    @PostMapping("purchase")
    public void addProductToCart(final @RequestBody CartRequestDTO cartRequestDTO) {
        cartProductInventoryService.addProductToCart(cartRequestDTO);
    }

    @PutMapping("{cartProductInventoryId}/increase-quantity")
    public void increaseQuantity(@PathVariable final UUID cartProductInventoryId) {
        cartService.increaseQuantity(cartProductInventoryId);
    }

    @PutMapping("{cartProductInventoryId}/decrease-quantity")
    public void decreaseQuantity(@PathVariable final UUID cartProductInventoryId) {
        cartService.decreaseQuantity(cartProductInventoryId);
    }

    @DeleteMapping("{cartProductInventoryId}")
    public void deleteProductFromCart(@PathVariable final UUID cartProductInventoryId) {
        cartService.deleteById(cartProductInventoryId);
    }
}
