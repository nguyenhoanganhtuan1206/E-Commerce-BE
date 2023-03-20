package com.ecommerce.domain.role;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class RoleError {

    public static <T> Supplier<NotFoundException> supplyRoleNotFound(final T input) {
        return () -> new NotFoundException("Role with %s not found", input);
    }
}
