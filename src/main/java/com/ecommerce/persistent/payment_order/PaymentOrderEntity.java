package com.ecommerce.persistent.payment_order;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_status.PaymentStatus;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_order")
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private long totalPrice;

    private Instant orderedAt;

    private Instant deliveryAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private String paymentMethodName;

    private UUID cartId;
}
