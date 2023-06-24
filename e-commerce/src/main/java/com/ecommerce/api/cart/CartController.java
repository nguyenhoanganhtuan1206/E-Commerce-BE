package com.ecommerce.api.cart;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.cart.mapper.CartDetailMapperDTO.toCartDetailDTOs;

@RestController
@RequestMapping(value = "/api/v1/carts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','SELLER')")
public class CartController {

    private final CartService cartService;

    @GetMapping("details")
    public List<CartDetailResponseDTO> findCartByUserId() {
        return toCartDetailDTOs(cartService.findCartByCurrentUserId());
    }

    @GetMapping("{sellerId}/user/cart-details")
    public List<CartDetailResponseDTO> findByUserIdAndSellerId(@PathVariable final UUID sellerId) {
        return toCartDetailDTOs(cartService.findByUserIdAndSellerId(sellerId));
    }

    @PostMapping("add-to-cart")
    public void addProductToCart(@RequestBody final CartRequestDTO cartRequestDTO) {
        cartService.addProductToCart(cartRequestDTO);
    }

    @PutMapping("{cartId}/increase-quantity")
    public void increaseQuantity(@PathVariable final UUID cartId) {
        cartService.increaseQuantity(cartId);
    }

    @PutMapping("{cartId}/decrease-quantity")
    public void decreaseQuantity(@PathVariable final UUID cartId) {
        cartService.decreaseQuantity(cartId);
    }

    @DeleteMapping("{cartId}")
    public void deleteProductFromCart(@PathVariable final UUID cartId) {
        cartService.deleteById(cartId);
    }
}
