package com.ecommerce.domain.payment_order;

import com.ecommerce.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class PaymentOrderError {

    public static <T> Supplier<BadRequestException> supplyExceedsCurrentQuantity(final String preMessage, final Object quantity) {
        return () -> new BadRequestException("Product %s only has %s units remaining. Please decrease the quantity in your cart", preMessage, quantity);
    }
}
