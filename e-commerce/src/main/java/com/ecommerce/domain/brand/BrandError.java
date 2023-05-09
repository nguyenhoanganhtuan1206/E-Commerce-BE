package com.ecommerce.domain.brand;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class BrandError {

    public static Supplier<NotFoundException> supplyBrandNotFound(final String fieldName, final String fieldValue) {
        return () -> new NotFoundException("Brand with %s %s cannot found", fieldName, fieldValue);
    }
}
