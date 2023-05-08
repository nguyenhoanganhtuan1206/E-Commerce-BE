package com.ecommerce.domain.style;

import com.ecommerce.error.ConflictException;
import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ProductStyleError {

    public static Supplier<NotFoundException> supplyProductStyleNotFound(final String fieldName, final String fieldValue) {
        return () -> new NotFoundException("Product with %s %s cannot be found", fieldName, fieldValue);
    }

    public static Supplier<ConflictException> supplyProductStyleExisted(final String fieldName, final String fieldValue) {
        return () -> new ConflictException("Product Style with %s %s has been taken", fieldName, fieldValue);
    }
}
