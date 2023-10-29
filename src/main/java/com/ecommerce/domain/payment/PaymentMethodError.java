package com.ecommerce.domain.payment;

import com.ecommerce.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class PaymentMethodError {

    public static Supplier<NotFoundException> supplyPaymentMethodNotFound(final String name) {
        return () -> new NotFoundException("Payment method with %s", name);
    }
}
