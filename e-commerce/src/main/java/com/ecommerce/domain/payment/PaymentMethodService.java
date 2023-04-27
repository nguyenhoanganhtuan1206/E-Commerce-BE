package com.ecommerce.domain.payment;

import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.persistent.paymentMethod.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.payment.PaymentMethodError.supplyPaymentMethodNotFound;
import static com.ecommerce.domain.payment.dto.PaymentMethodDTOMapper.toPaymentMethodDTO;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodDTO findByName(final String name) {
        return toPaymentMethodDTO(paymentMethodRepository.findByNameIgnoreCase(name)
                .orElseThrow(supplyPaymentMethodNotFound(name)));
    }
}
