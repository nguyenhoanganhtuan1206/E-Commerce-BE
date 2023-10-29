package com.ecommerce.domain.payment_order.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderRequestDTO {

    private List<UUID> cartIds;

    private String username;

    private String address;

    private String phoneNumber;

    private String emailAddress;

    private String location;

    private String paymentMethod;
}
