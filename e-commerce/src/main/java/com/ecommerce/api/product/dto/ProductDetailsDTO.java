package com.ecommerce.api.product.dto;

import com.ecommerce.domain.inventory.dto.InventoryDetailResponseDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.status.Status;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {

    private UUID id;

    private String name;

    private double price;

    private long quantity;

    private Status productApproval;

    private String description;

    private SellerDTO seller;

    private Long amountSoldOut;

    private Instant createdAt;

    private Instant updatedAt;

    private List<String> categories;

    private String brandName;

    private String variantName;

    private List<String> paymentMethods;

    private InventoryDetailResponseDTO inventory;

    private List<String> productStyles;
}
