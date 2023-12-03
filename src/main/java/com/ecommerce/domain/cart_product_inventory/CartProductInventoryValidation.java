package com.ecommerce.domain.cart_product_inventory;

import com.ecommerce.domain.cart.dto.CartRequestDTO;
import lombok.experimental.UtilityClass;

import static com.ecommerce.domain.cart.CartError.supplyCartValidation;
import static com.ecommerce.error.CommonError.supplyValidationError;

@UtilityClass
public class CartProductInventoryValidation {

    public static void validateCartRequestNotEmpty(final CartRequestDTO cartRequestDTO) {
        if (cartRequestDTO.getInventoryId() == null && cartRequestDTO.getProductId() == null) {
            throw supplyCartValidation("Your cart is currently empty! Please select some products.").get();
        }

        if (cartRequestDTO.getSellerId() == null) {
            throw supplyValidationError("Seller ID cannot be empty.").get();
        }
    }
}
