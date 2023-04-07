package com.ecommerce.domain.location;

import com.ecommerce.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class LocationError {

    public static Supplier<BadRequestException> supplyLocationAlreadyExisted() {
        return () -> new BadRequestException("Location you provided already existed, Please choose different");
    }

    public static <T> Supplier<BadRequestException> supplyLocationNotFound(final T input) {
        return () -> new BadRequestException("Location with %s cannot found", input);
    }
}
