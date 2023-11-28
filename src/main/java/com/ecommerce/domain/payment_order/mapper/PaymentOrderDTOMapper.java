package com.ecommerce.domain.payment_order.mapper;

import com.ecommerce.domain.payment_order.dto.PaymentOrderDTO;
import com.ecommerce.persistent.payment_order.PaymentOrderEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentOrderDTOMapper {

    public static PaymentOrderDTO toPaymentOrderDTO(final PaymentOrderEntity paymentOrder) {
        return PaymentOrderDTO.builder()
                .id(paymentOrder.getId())
                .totalPrice(paymentOrder.getTotalPrice())
                .orderedAt(paymentOrder.getOrderedAt())
                .deliveryAt(paymentOrder.getDeliveryAt())
                .paymentStatus(paymentOrder.getPaymentStatus())
                .deliveryStatus(paymentOrder.getDeliveryStatus())
                .paymentMethodName(paymentOrder.getPaymentMethodName())
                .cartId(paymentOrder.getCartId())
                .build();
    }
}
