package com.ecommerce.domain.payment_order.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderRequestDTO {

    private UUID cartId;

    private String paymentMethodName;
}