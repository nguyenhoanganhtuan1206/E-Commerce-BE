package com.ecommerce.error;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CommonError {

    public static Supplier<BadRequestException> supplyValidationError(final String message) {
        return () -> new BadRequestException(message);
    }
}
