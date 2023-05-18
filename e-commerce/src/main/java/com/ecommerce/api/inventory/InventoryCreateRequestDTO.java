package com.ecommerce.api.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryCreateRequestDTO {

    private String colorName;

    private String colorValue;

    private String sizeName;

    private String sizeValue;

    private int quantity;

    private long price;
}
