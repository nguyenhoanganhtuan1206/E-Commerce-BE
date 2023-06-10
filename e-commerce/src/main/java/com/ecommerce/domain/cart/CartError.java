package com.ecommerce.domain.cart;

import com.ecommerce.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CartError {

    public static Supplier<BadRequestException> supplyCartEmpty() {
        return () -> new BadRequestException("Your cart is currently empty! Please select some products.");
    }
}
