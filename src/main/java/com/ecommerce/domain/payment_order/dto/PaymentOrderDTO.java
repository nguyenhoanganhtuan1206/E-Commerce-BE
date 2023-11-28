package com.ecommerce.domain.payment_order.dto;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_status.PaymentStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderDTO {

    private UUID id;

    private long totalPrice;

    private Instant orderedAt;

    private Instant deliveryAt;

    private PaymentStatus paymentStatus;

    private DeliveryStatus deliveryStatus;

    private String paymentMethodName;

    private UUID cartId;
}
