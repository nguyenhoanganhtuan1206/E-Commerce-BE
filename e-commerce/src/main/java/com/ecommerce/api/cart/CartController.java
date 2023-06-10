package com.ecommerce.api.cart;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.cart.dto.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ecommerce.domain.cart.mapper.CartMapperDTO.toCartResponseDTO;

@RestController
@RequestMapping(value = "/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAnyRole('USER','SELLER')")
    @PostMapping("add-to-cart")
    public CartResponseDTO addProductToCart(@RequestBody final CartRequestDTO cartRequestDTO) {
        return toCartResponseDTO(cartService.addProductToCart(cartRequestDTO));
    }
}
