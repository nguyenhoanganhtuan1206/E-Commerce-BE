package com.ecommerce.domain.orders.dto;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.inventory.dto.InventoryResponseDTO;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.payment_status.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {

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
