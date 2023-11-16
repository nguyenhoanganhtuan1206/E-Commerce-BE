package com.ecommerce.domain.payment_order.dto;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.inventory.dto.InventoryResponseDTO;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.payment_status.PaymentStatus;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderDetailDTO {

    private UUID id;

    private String productName;

    private double totalPrice;

    private long quantity;

    private Instant orderedAt;

    private DeliveryStatus deliveryStatus;

    private String username;

    private String email;

    private String phoneNumber;

    private String location;

    private String address;

    private PaymentStatus paymentStatus;

    private PaymentMethodDTO paymentMethod;

    private List<String> categories;

    private List<InventoryResponseDTO> inventories;

    private List<String> productStyles;
}
