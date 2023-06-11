package com.ecommerce.api.product;

import com.ecommerce.api.product.dto.*;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.product.user.UserProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTO;
import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTOs;
import static com.ecommerce.domain.product.mapper.ProductDetailDTOMapper.toProductsResponseDetailDTOs;
import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    public final UserProductService userProductService;

    public final CommonProductService commonProductService;

    @GetMapping
    public List<ProductResponseDetailDTO> findAllSortedByAmountSoldOut() {
        return toProductsResponseDetailDTOs(commonProductService.findAllSortedByAmountSoldOut());
    }

    @GetMapping("search")
    public List<ProductDTO> searchProducts(final @RequestParam String searchTemp) {
        return commonProductService.findByNameOrSellerName(searchTemp);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/user")
    public List<ProductResponseDTO> findByCurrentUserId() {
        return userProductService.findByCurrentUserId();
    }

    @GetMapping("{productId}/productDetail")
    public ProductDetailsDTO findProductDetailById(@PathVariable final UUID productId) {
        return commonProductService.findProductDetailById(productId);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @GetMapping("{productId}")
    public ProductResponseDTO findProductById(@PathVariable final UUID productId) {
        return toProductResponseDTO(commonProductService.findById(productId));
    }


    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PostMapping
    public ProductResponseDTO create(
            final @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        return userProductService.create(productCreateRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PutMapping("{productId}")
    public ProductResponseDTO update(
            final @Valid @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO,
            final @PathVariable UUID productId,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        return userProductService.update(productId, productUpdateRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @DeleteMapping("{productId}")
    public void delete(final @PathVariable UUID productId) {
        userProductService.delete(productId);
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("out-of-stock")
    public List<ProductResponseDTO> findProductWithOutOfStockAndSellerId() {
        return toProductResponseDTOs(userProductService.findProductWithOutOfStockAndSellerId());
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("in-stock")
    public List<ProductResponseDTO> findProductWithInStockAndSellerId() {
        return toProductResponseDTOs(userProductService.findProductWithInStockAndSellerId());
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("approval")
    public List<ProductResponseDTO> findProductWithApproval() {
        return toProductResponseDTOs(userProductService.findProductWithApproval());
    }
}
