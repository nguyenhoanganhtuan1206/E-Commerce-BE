package com.ecommerce.domain.cart_product_inventory;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class CartProductInventoryError {

    public static Supplier<NotFoundException> supplyCartProductInventoryNotFound(final Object fieldValue, final Object fieldName) {
        return () -> new NotFoundException("This cart with %s %s cannot found", fieldName, fieldValue);
    }
}
