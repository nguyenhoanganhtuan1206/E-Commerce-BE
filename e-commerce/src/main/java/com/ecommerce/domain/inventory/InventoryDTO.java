package com.ecommerce.domain.inventory;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {

    private UUID id;

    private int quantity;

    private Instant createdAt;

    private Instant updatedAt;
}
