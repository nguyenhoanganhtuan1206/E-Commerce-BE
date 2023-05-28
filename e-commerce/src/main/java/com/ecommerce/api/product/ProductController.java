package com.ecommerce.api.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.api.product.dto.ProductUpdateRequestDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTO;
import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    public final ProductService productService;

    @GetMapping("search")
    public List<ProductDTO> searchProducts(final @RequestParam String searchTemp) {
        return productService.findByNameOrSellerName(searchTemp);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public List<ProductResponseDTO> findByCurrentUserId() {
        return productService.findByCurrentUserId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("{productId}")
    public ProductResponseDTO findById(@PathVariable final UUID productId) {
        return toProductResponseDTO(productService.findById(productId));
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PostMapping
    public ProductResponseDTO create(
            final @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        return productService.create(productCreateRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PutMapping("{productId}")
    public ProductResponseDTO update(
            final @Valid @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO,
            final @PathVariable UUID productId,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        return productService.update(productId, productUpdateRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @DeleteMapping("{productId}")
    public void delete(final @PathVariable UUID productId) {
        productService.delete(productId);
    }
}
