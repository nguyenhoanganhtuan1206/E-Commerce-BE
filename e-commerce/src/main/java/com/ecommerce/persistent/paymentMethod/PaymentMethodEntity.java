package com.ecommerce.persistent.paymentMethod;

import com.ecommerce.persistent.seller.SellerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "paymentMethods",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.ALL})
    private Set<SellerEntity> sellers;

    public PaymentMethodEntity(String name) {
        this.name = name;
    }
}
