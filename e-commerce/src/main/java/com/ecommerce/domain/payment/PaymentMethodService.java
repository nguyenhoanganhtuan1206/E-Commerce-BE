package com.ecommerce.domain.payment;

import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.persistent.paymentMethod.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.payment.PaymentMethodError.supplyPaymentMethodNotFound;
import static com.ecommerce.domain.payment.dto.PaymentMethodDTOMapper.toPaymentMethodDTO;
import static com.ecommerce.domain.payment.dto.PaymentMethodDTOMapper.toPaymentMethodEntity;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodDTO save(final PaymentMethodDTO paymentMethodDTO) {
        return toPaymentMethodDTO(paymentMethodRepository.save(toPaymentMethodEntity(paymentMethodDTO)));
    }

    public PaymentMethodDTO findByName(final String name) {
        return toPaymentMethodDTO(paymentMethodRepository.findByName(name)
                .orElseThrow(supplyPaymentMethodNotFound(name)));
    }
}
