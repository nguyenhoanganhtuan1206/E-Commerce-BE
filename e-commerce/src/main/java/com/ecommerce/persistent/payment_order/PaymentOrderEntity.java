package com.ecommerce.persistent.payment_order;

import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_status.PaymentStatus;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.payment_method.PaymentMethodEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private long totalPrice;

    private Instant orderedAt;

    private Instant deliveryAt;

    private PaymentStatus paymentStatus;

    private DeliveryStatus deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethodEntity paymentMethod;

    @OneToMany(mappedBy = "paymentOrder", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private List<CartEntity> carts;
}
