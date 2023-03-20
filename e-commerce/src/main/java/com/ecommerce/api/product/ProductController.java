package com.ecommerce.api.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    public final ProductService productService;

//    @GetMapping
//    public Set<ProductDTO> searchProducts(final @RequestParam String searchTemp) {
//        return productService
//    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
    @GetMapping("{sellerId}")
    public Set<ProductResponseDTO> findBySellerId(final @PathVariable UUID sellerId) {
        return productService.findBySellerId(sellerId);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PostMapping("{sellerId}")
    public ProductResponseDTO create(
            final @PathVariable UUID sellerId,
            final @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        return productService.create(sellerId, productCreateRequestDTO);
    }
}
