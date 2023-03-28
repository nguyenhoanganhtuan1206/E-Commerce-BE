package com.ecommerce.error;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CommonError {

    public static Supplier<BadRequestException> supplyValidationError(final String message) {
        return () -> new BadRequestException(message);
    }

    public static Supplier<UnauthorizedException> supplyUnauthorizedException(final String message) {
        return () -> new UnauthorizedException(message);
    }

    public static Supplier<AccessDeniedException> supplyAccessDeniedException(final String message) {
        return () -> new AccessDeniedException(message);
    }
}
