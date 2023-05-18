package com.ecommerce.api.admin.product;

import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/products")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> findAll() {
        return productService.findAll();
    }
}
