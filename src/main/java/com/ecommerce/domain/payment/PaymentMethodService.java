package com.ecommerce.domain.payment;

import com.ecommerce.persistent.payment_method.PaymentMethodEntity;
import com.ecommerce.persistent.payment_method.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.payment.PaymentMethodError.supplyPaymentMethodNotFound;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodEntity findByName(final String name) {
        return paymentMethodRepository.findByNameIgnoreCase(name)
                .orElseThrow(supplyPaymentMethodNotFound(name));
    }
}
