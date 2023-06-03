package com.ecommerce.api.admin.product;

import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.api.product.dto.ProductResponseDetailDTO;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.product.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTO;
import static com.ecommerce.domain.product.mapper.ProductDetailDTOMapper.toProductsResponseDetailDTOs;

@RestController
@RequestMapping(value = "/api/v1/admin/products")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final CommonProductService commonProductService;

    private final AdminProductService adminProductService;

    @GetMapping
    public List<ProductResponseDetailDTO> findAll() {
        return toProductsResponseDetailDTOs(commonProductService.findALlSorted());
    }

    @PutMapping("/{productId}/approval")
    public ProductResponseDTO approvalProduct(@PathVariable final UUID productId) {
        return toProductResponseDTO(adminProductService.approvalProduct(productId));
    }

    @PutMapping("/{productId}/deactivate")
    public ProductResponseDTO deactivateProduct(@PathVariable final UUID productId, final @RequestBody Map<String, String> requestBody) {
        return toProductResponseDTO(adminProductService.deActivateProduct(productId, requestBody.get("contentFeedback")));
    }
}
