package com.ecommerce.api.order.dto;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderDeliveryStatusRequestDTO {

    private UUID orderPaymentId;

    private DeliveryStatus deliveryStatus;
}
