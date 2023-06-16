package com.ecommerce.domain.cart.dto;

import com.ecommerce.domain.product.dto.ProductDTO;
import com.ecommerce.domain.user.UserDTO;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailResponseDTO {

    private UUID id;

    private int quantity;

    private double totalPrice;

    private Instant createdAt;

    private UUID sellerId;

    private ProductDTO product;

    private UserDTO user;
}
