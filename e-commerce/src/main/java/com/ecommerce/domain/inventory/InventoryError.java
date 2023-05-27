package com.ecommerce.domain.inventory;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class InventoryError {

    public static Supplier<NotFoundException> supplyInventoryNotFound(final Object fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Inventory with %s %s cannot be found", fieldName, fieldValue);
    }
}
