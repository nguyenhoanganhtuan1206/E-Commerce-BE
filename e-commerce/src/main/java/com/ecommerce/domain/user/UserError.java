package com.ecommerce.domain.user;

import com.ecommerce.error.BadRequestException;
import com.ecommerce.error.ConflictException;
import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class UserError {

    public static <T> Supplier<NotFoundException> supplyUserNotFound(final String fieldName, final T fieldValue) {
        return () -> new NotFoundException("User with %s %s cannot found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplierCodeResetPasswordInvalid(final String message) {
        return () -> new BadRequestException(message);
    }

    public static Supplier<BadRequestException> supplyUserExisted(final String email) {
        return () -> new BadRequestException("User with email %s has been taken", email);
    }

    public static Supplier<ConflictException> supplyConflictError(final String message) {
        return () -> new ConflictException(message);
    }
}
