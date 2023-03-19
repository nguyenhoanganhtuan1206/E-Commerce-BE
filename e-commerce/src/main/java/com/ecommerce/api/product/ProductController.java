package com.ecommerce.api.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    public final ProductService productService;

    @PostMapping("{userId}")
    public ProductResponseDTO create(
            final @PathVariable UUID userId,
            final @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO,
            final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return productService.create(userId, productCreateRequestDTO);
    }
}
