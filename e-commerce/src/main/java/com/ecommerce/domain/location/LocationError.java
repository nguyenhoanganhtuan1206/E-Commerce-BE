package com.ecommerce.domain.location;

import com.ecommerce.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class LocationError {

    public static Supplier<BadRequestException> supplyAddressAvailable(final String address) {
        return () -> new BadRequestException("Address with %s is existed", address);
    }
}
