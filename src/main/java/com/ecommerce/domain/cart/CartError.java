package com.ecommerce.domain.cart;

import com.ecommerce.error.BadRequestException;
import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CartError {

    public static Supplier<NotFoundException> supplyCartNotFound(final Object fieldValue, final Object fieldName) {
        return () -> new NotFoundException("Cart with %s %s cannot found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyCartValidation(final String message) {
        return () -> new BadRequestException(message);
    }
}
