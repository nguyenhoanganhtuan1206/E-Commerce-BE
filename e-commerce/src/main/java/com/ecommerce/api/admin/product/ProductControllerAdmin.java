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
        return toProductsResponseDetailDTOs(commonProductService.findAllByCreatedAt());
    }

    @PutMapping("/{productId}/approval")
    public ProductResponseDTO approvalProduct(@PathVariable final UUID productId) {
        return toProductResponseDTO(adminProductService.approvalProduct(productId));
    }

    @PostMapping("{productId}/feedback")
    public void sendFeedbackToSeller(@PathVariable final UUID productId, @RequestBody final Map<String, String> requestBody) {
        adminProductService.sendFeedbackAboutProductToUser(productId, requestBody.get("contentFeedback"));
    }

    @PutMapping("/{productId}/deactivate")
    public ProductResponseDTO deactivateProduct(@PathVariable final UUID productId) {
        return toProductResponseDTO(adminProductService.deActivateProduct(productId));
    }
}
