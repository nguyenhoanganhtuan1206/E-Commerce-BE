package com.ecommerce.domain.category;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CategoryError {

    public static <T> Supplier<NotFoundException> supplyCategoryNotFound(final T input) {
        return () -> new NotFoundException("Category with %s cannot found", input);
    }
}
