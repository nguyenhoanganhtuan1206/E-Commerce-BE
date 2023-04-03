package com.ecommerce.domain.variant;

import com.ecommerce.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class VariantError {

    public static <T> Supplier<BadRequestException> supplyVariantNotFound(final T input) {
        return () -> new BadRequestException("Category Variant with %s not found", input);
    }
}
