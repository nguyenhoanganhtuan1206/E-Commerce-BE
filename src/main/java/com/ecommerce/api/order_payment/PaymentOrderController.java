package com.ecommerce.api.order_payment;

import com.ecommerce.domain.payment_order.PaymentOrderService;
import com.ecommerce.domain.payment_order.dto.PaymentOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/payment-order")
@RequiredArgsConstructor
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public void createPayment(final @RequestBody PaymentOrderRequestDTO paymentOrderRequestDTO) {
        paymentOrderService.createPaymentOrder(paymentOrderRequestDTO);
    }
}