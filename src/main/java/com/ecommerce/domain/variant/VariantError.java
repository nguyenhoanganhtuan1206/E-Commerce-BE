package com.ecommerce.domain.variant;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class VariantError {

    public static Supplier<NotFoundException> supplyVariantNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Category Variant with %s %s not found", fieldName, fieldValue);
    }
}
