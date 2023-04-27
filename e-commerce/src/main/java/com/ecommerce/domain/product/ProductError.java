package com.ecommerce.domain.product;

import com.ecommerce.error.ConflictException;
import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ProductError {

    public static Supplier<NotFoundException> supplyProductNotFound(final String fieldName, final String fieldValue) {
        return () -> new NotFoundException("Product with %s %s cannot be found", fieldName, fieldValue);
    }

    public static Supplier<ConflictException> supplyProductExisted(final String fieldName, final String fieldValue) {
        return () -> new ConflictException("Product with %s %s has been taken", fieldName, fieldValue);
    }
}
