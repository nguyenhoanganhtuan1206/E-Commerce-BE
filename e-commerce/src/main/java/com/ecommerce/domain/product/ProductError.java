package com.ecommerce.domain.product;

import com.ecommerce.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ProductError {

    public static Supplier<BadRequestException> supplyProductExisted(final String name) {
        return () -> new BadRequestException("Product with %s has been taken", name);
    }
}
