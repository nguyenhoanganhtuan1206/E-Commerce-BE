package com.ecommerce.domain.product.dto;

import com.ecommerce.api.category.dto.CategoryResponseDTO;
import com.ecommerce.api.style.dto.ProductStyleResponseDTO;
import com.ecommerce.domain.brand.BrandDTO;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductCartDetailDTO {

    private UUID id;

    private String name;

    private double price;

    private long quantity;

    private List<CategoryResponseDTO> categories;

    private CategoryVariantDTO categoryVariant;

    private BrandDTO brand;

    private SellerDTO seller;

    private List<PaymentMethodDTO> paymentMethods;

    private List<ProductStyleResponseDTO> productStyles;
}
