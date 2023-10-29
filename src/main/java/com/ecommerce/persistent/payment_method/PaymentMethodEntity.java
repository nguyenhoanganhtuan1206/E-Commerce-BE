package com.ecommerce.persistent.payment_method;

import com.ecommerce.persistent.payment_order.PaymentOrderEntity;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "paymentMethods")
    private Set<ProductEntity> products;

    @OneToMany(mappedBy = "paymentMethod")
    private List<PaymentOrderEntity> paymentOrders;

    public PaymentMethodEntity(String name) {
        this.name = name;
    }
}
