package com.ecommerce.api.cart_product_inventory;

import com.ecommerce.domain.cart_product_inventory.CartProductInventoryService;
import com.ecommerce.domain.cart_product_inventory.dto.CartProductInventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.cart_product_inventory.mapper.CartProductInventoryDTOMapper.toCartProductInventoryDTOs;

@RestController
@RequestMapping(value = "/api/v1/cart-product-inventory")
@RequiredArgsConstructor
public class CartProductInventoryController {

    public final CartProductInventoryService cartProductInventoryService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("cart/{cartId}")
    public List<CartProductInventoryDTO> findByCartId(final @PathVariable UUID cartId) {
        return toCartProductInventoryDTOs(cartProductInventoryService.findByCartId(cartId));
    }
}
