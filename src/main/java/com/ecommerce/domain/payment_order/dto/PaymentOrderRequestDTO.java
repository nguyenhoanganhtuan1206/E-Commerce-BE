package com.ecommerce.domain.payment_order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentOrderRequestDTO {

    private UUID cartId;

    private String paymentMethodName;
}