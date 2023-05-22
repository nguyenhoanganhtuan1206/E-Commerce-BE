package com.ecommerce.api.product.dto;

import com.ecommerce.api.inventory.InventoryResponseDTO;
import com.ecommerce.persistent.status.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDTO {

    private UUID id;

    private String name;

    private double price;

    private long quantity;

    private Status productApproval;

    private String description;

    private UUID sellerId;

    private List<String> categories;

    private String brandName;

    private String variantName;

    private List<String> paymentMethods;

    private List<InventoryResponseDTO> inventories;

    private List<String> productStyles;
}
