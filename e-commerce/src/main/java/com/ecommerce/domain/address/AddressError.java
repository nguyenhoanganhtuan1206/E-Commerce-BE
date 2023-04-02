package com.ecommerce.domain.address;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class AddressError {

    public static Supplier<NotFoundException> supplyAddressNotFound(final String message) {
        return () -> new NotFoundException(message);
    }
}
