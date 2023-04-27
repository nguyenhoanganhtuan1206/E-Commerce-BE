package com.ecommerce.domain.seller;

import com.ecommerce.error.ConflictException;
import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class SellerError {

    public static Supplier<NotFoundException> supplySellerNotFound(final String fieldName, final String fieldValue) {
        return () -> new NotFoundException("Seller with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<ConflictException> supplyEmailSellerUsedError(final String emailSeller) {
        return () -> new ConflictException("Email with %s is already registered as a seller. Please use a different email address to register", emailSeller);
    }
}
