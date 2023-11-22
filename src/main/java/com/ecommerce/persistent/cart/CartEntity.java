package com.ecommerce.persistent.cart;

import com.ecommerce.persistent.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cart")
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private long totalPrice;

    private boolean isPayment;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private UUID sellerId;
}
