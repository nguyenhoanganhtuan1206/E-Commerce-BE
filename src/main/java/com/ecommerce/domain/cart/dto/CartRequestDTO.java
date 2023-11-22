package com.ecommerce.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartRequestDTO {

    private UUID sellerId;

    private UUID productId;

    private UUID inventoryId;

    private int quantity;
}
