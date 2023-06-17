package com.ecommerce.domain.product.dto;

import com.ecommerce.api.category.dto.CategoryResponseDTO;
import com.ecommerce.api.style.dto.ProductStyleResponseDTO;
import com.ecommerce.domain.brand.BrandDTO;
import com.ecommerce.domain.inventory.dto.InventoryDTO;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.persistent.status.Status;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
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

    private BrandDTO brand;

    private InventoryDTO inventory;

    private CategoryVariantDTO categoryVariant;

    private List<CategoryResponseDTO> categories;

    private List<PaymentMethodDTO> paymentMethods;

    private List<ProductStyleResponseDTO> productStyles;
}
