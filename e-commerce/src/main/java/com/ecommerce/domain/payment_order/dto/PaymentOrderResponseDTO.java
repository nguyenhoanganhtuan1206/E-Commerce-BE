package com.ecommerce.domain.payment_order.dto;

import com.ecommerce.domain.cart.dto.CartResponseDTO;
import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_status.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
public class PaymentOrderResponseDTO {

    private UUID id;

    private long totalPrice;

    private Instant orderedAt;

    private Instant deliveryAt;

    private PaymentStatus paymentStatus;

    private DeliveryStatus deliveryStatus;

    private String paymentMethod;

    private List<CartResponseDTO> carts;
}
