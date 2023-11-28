package com.ecommerce.domain.payment_order;

import com.ecommerce.error.BadRequestException;
import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class PaymentOrderError {

    public static Supplier<BadRequestException> supplyExceedsCurrentQuantity(final String preMessage, final Object quantity) {
        return () -> new BadRequestException("Product %s only has %s units remaining. Please decrease the quantity in your cart", preMessage, quantity);
    }

    public static Supplier<NotFoundException> supplyPaymentOrderNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Payment Order with %s %s cannot be found", fieldName, fieldValue);
    }
}
