package com.ecommerce.domain.product;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.domain.seller.SellerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private UUID id;

    private String name;

    private long price;

    private String condition;

    private boolean productStatus;

    private String description;

    private SellerDTO seller;

    private CategoryDTO category;
}
