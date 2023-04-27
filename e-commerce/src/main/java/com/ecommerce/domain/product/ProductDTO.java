package com.ecommerce.domain.product;

import com.ecommerce.domain.inventory.InventoryDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.persistent.status.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private UUID id;

    private String name;

    private long price;

    private String condition;

    private Status productApproval;

    private String description;

    private SellerDTO seller;

    private CategoryVariantDTO categoryVariant;

    private InventoryDTO inventory;
}
