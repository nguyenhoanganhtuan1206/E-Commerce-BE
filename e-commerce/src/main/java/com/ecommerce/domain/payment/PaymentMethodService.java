package com.ecommerce.domain.payment;

import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.paymentMethod.PaymentMethodRepository;
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
