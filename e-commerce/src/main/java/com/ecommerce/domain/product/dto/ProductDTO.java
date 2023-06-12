package com.ecommerce.domain.product.dto;

import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.persistent.status.Status;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private UUID id;

    private String name;

    private double price;

    private long quantity;

    private Status productApproval;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private Long amountSoldOut;

    private SellerDTO seller;

    private CategoryVariantDTO categoryVariant;
}
