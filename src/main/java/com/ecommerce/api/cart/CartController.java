package com.ecommerce.api.cart;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.cart_product_inventory.CartProductInventoryService;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
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

    @GetMapping("{sellerId}/user/cart-details")
    public List<CartDetailResponseDTO> findByUserIdAndSellerId(@PathVariable final UUID sellerId) {
        return cartProductInventoryService.findByUserIdAndSellerId(sellerId);
    }

    @PostMapping("add-to-cart")
    public CartProductInventoryEntity addProductToCart(@RequestBody final CartRequestDTO cartRequestDTO) {
        return cartProductInventoryService.addProductToCart(cartRequestDTO);
    }

    @PutMapping("{cartId}/increase-quantity")
    public void increaseQuantity(@PathVariable final UUID cartId) {
        cartProductInventoryService.increaseQuantity(cartId);
    }

    @PutMapping("{cartId}/decrease-quantity")
    public void decreaseQuantity(@PathVariable final UUID cartId) {
        cartProductInventoryService.decreaseQuantity(cartId);
    }

    @DeleteMapping("{cartId}")
    public void deleteProductFromCart(@PathVariable final UUID cartId) {
        cartService.deleteById(cartId);
    }
}
