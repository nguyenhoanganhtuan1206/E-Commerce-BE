package com.ecommerce.api.cart;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart.dto.CartDetailResponseDTO;
import com.ecommerce.domain.cart.dto.CartRequestDTO;
import com.ecommerce.domain.cart.dto.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ecommerce.domain.cart.mapper.CartDetailMapperDTO.toCartDetailDTOs;
import static com.ecommerce.domain.cart.mapper.CartMapperDTO.toCartResponseDTO;

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

    @PostMapping("add-to-cart")
    public CartResponseDTO addProductToCart(@RequestBody final CartRequestDTO cartRequestDTO) {
        return toCartResponseDTO(cartService.addProductToCart(cartRequestDTO));
    }
}
