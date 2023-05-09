package com.ecommerce.persistent.paymentMethod;

import com.ecommerce.persistent.product.ProductEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "paymentMethods")
    private Set<ProductEntity> products;

    public PaymentMethodEntity(String name) {
        this.name = name;
    }
}
