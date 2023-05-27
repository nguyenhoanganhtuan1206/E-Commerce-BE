package com.ecommerce.domain.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.api.product.dto.ProductUpdateRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.brand.BrandService;
import com.ecommerce.domain.category.CategoryService;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.seller.SellerService;
import com.ecommerce.domain.style.ProductStyleService;
import com.ecommerce.domain.variant.CategoryVariantService;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.status.Status;
import com.ecommerce.persistent.style.ProductStyleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryEntities;
import static com.ecommerce.domain.inventory.mapper.InventoryUpdateDTOMapper.toInventoryEntities;
import static com.ecommerce.domain.product.ProductError.supplyProductExisted;
import static com.ecommerce.domain.product.ProductError.supplyProductNotFound;
import static com.ecommerce.domain.product.ProductValidation.*;
import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTO;
import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTOs;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTOs;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final PaymentMethodService paymentMethodService;

    private final CategoryVariantService categoryVariantService;

    private final BrandService brandService;

    private final CategoryService categoryService;

    private final ProductStyleService productStyleService;

    private final SellerService sellerService;

    private final InventoryService inventoryService;

    private final AuthsProvider authsProvider;

    public ProductEntity findById(final UUID id) {
        return productRepository.findById(id)
                .orElseThrow(supplyProductNotFound("id", id));
    }

    public List<ProductResponseDTO> findAll() {
        return toProductResponseDTOs(productRepository.findAll());
    }

    public List<ProductResponseDTO> findByCurrentUserId() {
        return toProductResponseDTOs(productRepository.findByUserId(authsProvider.getCurrentUserId()));
    }

    public List<ProductDTO> findByNameOrSellerName(final String searchTemp) {
        return toProductDTOs(productRepository.findByNameOrSellerName(searchTemp));
    }

    public ProductResponseDTO create(final ProductCreateRequestDTO productRequestDTO) {
        final SellerEntity seller = sellerService.findByUserId(authsProvider.getCurrentUserId());
        validatePaymentMethodNotEmpty(productRequestDTO.getPaymentMethods());
        validateCategoriesNotEmpty(productRequestDTO.getCategories());
        validateProductStylesNotEmpty(productRequestDTO.getProductStyles());

        verifyIfProductAvailable(productRequestDTO.getName());

        final ProductEntity productCreate = buildProductEntity(productRequestDTO, seller);

        if (!productRequestDTO.getInventories().isEmpty()) {
            inventoryService.createInventories(toInventoryEntities(productRequestDTO.getInventories()), productRepository.save(productCreate));

            return toProductResponseDTO(productCreate);
        } else {
            validatePriceProduct(productRequestDTO.getPrice());
            validateQuantityProduct(productRequestDTO.getQuantity());

            return toProductResponseDTO(productRepository.save(productCreate));
        }
    }

    public ProductResponseDTO update(final UUID productId, final ProductUpdateRequestDTO productRequestDTO) {
        final ProductEntity productUpdate = findById(productId);

        validatePaymentMethodNotEmpty(productRequestDTO.getPaymentMethods());
        validateCategoriesNotEmpty(productRequestDTO.getCategories());
        validateProductStylesNotEmpty(productRequestDTO.getProductStyles());

        return toProductResponseDTO(productRepository.save(updateCurrentProductProperties(productUpdate, productRequestDTO)));
    }

    private ProductEntity updateCurrentProductProperties(final ProductEntity currentProduct, final ProductUpdateRequestDTO productRequestDTO) {
        if (!currentProduct.getName().equals(productRequestDTO.getName())) {
            verifyIfProductAvailable(productRequestDTO.getName());

            currentProduct.setName(productRequestDTO.getName());
        }

        if (!productRequestDTO.getInventories().isEmpty()) {
            inventoryService.updateInventoryWithProductId(currentProduct.getId(), toInventoryEntities(productRequestDTO.getInventories()));
        } else {
            validatePriceProduct(productRequestDTO.getPrice());
            validateQuantityProduct(productRequestDTO.getQuantity());
        }

        return currentProduct
                .withDescription(productRequestDTO.getDescription())
                .withCategoryVariant(categoryVariantService.findByName(productRequestDTO.getVariantName()))
                .withBrand(brandService.findByBrandName(productRequestDTO.getBrandName()))
                .withCategories(findCategoriesByName(productRequestDTO.getCategories()))
                .withPrice(productRequestDTO.getPrice())
                .withPaymentMethods(findPaymentMethodsByName(productRequestDTO.getPaymentMethods()))
                .withProductStyles(findProductStyleByName(productRequestDTO.getProductStyles()))
                .withQuantity(productRequestDTO.getQuantity())
                .withProductApproval(Status.PENDING);
    }

    private ProductEntity buildProductEntity(final ProductCreateRequestDTO productRequestDTO, final SellerEntity seller) {
        return ProductEntity.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .productApproval(Status.PENDING)
                .categoryVariant(categoryVariantService.findByName(productRequestDTO.getVariantName()))
                .brand(brandService.findByBrandName(productRequestDTO.getBrandName()))
                .paymentMethods(findPaymentMethodsByName(productRequestDTO.getPaymentMethods()))
                .categories(findCategoriesByName(productRequestDTO.getCategories()))
                .productStyles(findProductStyleByName(productRequestDTO.getProductStyles()))
                .quantity(productRequestDTO.getQuantity())
                .price(productRequestDTO.getPrice())
                .seller(seller)
                .build();
    }

    private void verifyIfProductAvailable(final String name) {
        productRepository.findByNameContainingIgnoreCase(name).ifPresent(existingProduct -> {
            throw supplyProductExisted("name", name).get();
        });
    }

    private List<ProductStyleEntity> findProductStyleByName(final List<String> productStylesName) {
        return productStylesName.stream()
                .map(productStyleService::findByName)
                .toList();
    }

    private List<CategoryEntity> findCategoriesByName(final List<String> categories) {
        return categories.stream()
                .map(categoryService::findByCategoryName)
                .toList();
    }

    private List<PaymentMethodEntity> findPaymentMethodsByName(final List<String> paymentMethods) {
        return paymentMethods.stream()
                .map(paymentMethodService::findByName)
                .toList();
    }
}
