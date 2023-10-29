package com.ecommerce.domain.payment_order.mapper;

import com.ecommerce.domain.payment_order.dto.PaymentOrderResponseDTO;
import com.ecommerce.persistent.payment_order.PaymentOrderEntity;
import lombok.experimental.UtilityClass;

import static com.ecommerce.domain.cart.mapper.CartMapperDTO.toCartResponseDTOs;

@UtilityClass
public class PaymentOrderDTOMapper {

    public static PaymentOrderResponseDTO toPaymentOrderDTO(final PaymentOrderEntity paymentOrder) {
        return PaymentOrderResponseDTO.builder()
                .id(paymentOrder.getId())
                .paymentStatus(paymentOrder.getPaymentStatus())
                .deliveryStatus(paymentOrder.getDeliveryStatus())
                .paymentMethod(paymentOrder.getPaymentMethod().getName())
                .totalPrice(paymentOrder.getTotalPrice())
                .carts(toCartResponseDTOs(paymentOrder.getCarts()))
                .deliveryAt(paymentOrder.getDeliveryAt())
                .orderedAt(paymentOrder.getOrderedAt())
                .build();
    }
}
