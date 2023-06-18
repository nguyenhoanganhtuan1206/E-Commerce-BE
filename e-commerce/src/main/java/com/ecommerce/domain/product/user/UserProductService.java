package com.ecommerce.domain.product.user;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.api.product.dto.ProductUpdateRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.brand.BrandService;
import com.ecommerce.domain.category.CategoryService;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.inventory.mapper.InventoryUpdateDTOMapper;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.product.CommonProductService;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryEntities;
import static com.ecommerce.domain.product.ProductError.supplyProductExisted;
import static com.ecommerce.domain.product.ProductValidation.*;
import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTO;
import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.toProductResponseDTOs;

@Service
@RequiredArgsConstructor
public class UserProductService {

    private final ProductRepository productRepository;

    private final CommonProductService commonProductService;

    private final PaymentMethodService paymentMethodService;

    private final CategoryVariantService categoryVariantService;

    private final BrandService brandService;

    private final CategoryService categoryService;

    private final ProductStyleService productStyleService;

    private final SellerService sellerService;

    private final InventoryService inventoryService;

    private final AuthsProvider authsProvider;

    public List<ProductResponseDTO> findByCurrentUserId() {
        return toProductResponseDTOs(productRepository.findByUserId(authsProvider.getCurrentUserId()));
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
        }

        validatePriceProduct(productRequestDTO.getPrice());
        validateQuantityProduct(productRequestDTO.getQuantity());

        return toProductResponseDTO(productRepository.save(productCreate));
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
                .createdAt(Instant.now())
                .seller(seller)
                .build();
    }

    public ProductResponseDTO update(final UUID productId, final ProductUpdateRequestDTO productRequestDTO) {
        final ProductEntity productUpdate = commonProductService.findById(productId);

        validatePaymentMethodNotEmpty(productRequestDTO.getPaymentMethods());
        validateCategoriesNotEmpty(productRequestDTO.getCategories());
        validateProductStylesNotEmpty(productRequestDTO.getProductStyles());

        return toProductResponseDTO(productRepository.save(updateCurrentProductProperties(productUpdate, productRequestDTO)));
    }

    public void delete(final UUID productId) {
        final ProductEntity currentProduct = commonProductService.findById(productId);

        productRepository.delete(currentProduct);
    }

    public List<ProductEntity> findProductWithOutOfStockAndSellerId() {
        final SellerEntity seller = sellerService.findByUserId(authsProvider.getCurrentUserId());
        return productRepository.findProductWithOutOfStockAndSellerId(seller.getId());
    }

    public List<ProductEntity> findProductWithInStockAndSellerId() {
        final SellerEntity seller = sellerService.findByUserId(authsProvider.getCurrentUserId());
        return productRepository.findProductWithInStockAndSellerId(seller.getId());
    }

    public List<ProductEntity> findProductWithApproval() {
        final SellerEntity seller = sellerService.findByUserId(authsProvider.getCurrentUserId());
        return productRepository.findProductWithStatusAndSellerId(Status.ACTIVE, seller.getId());
    }

    private ProductEntity updateCurrentProductProperties(final ProductEntity currentProduct, final ProductUpdateRequestDTO productRequestDTO) {
        if (!currentProduct.getName().equals(productRequestDTO.getName())) {
            verifyIfProductAvailable(productRequestDTO.getName());

            currentProduct.setName(productRequestDTO.getName());
        }

        if (!productRequestDTO.getInventories().isEmpty()) {
            inventoryService.updateInventoryWithProductId(currentProduct.getId(), InventoryUpdateDTOMapper.toInventoryEntities(productRequestDTO.getInventories()));
        } else {
            validatePriceProduct(productRequestDTO.getPrice());
            validateQuantityProduct(productRequestDTO.getQuantity());
        }

        if (productRequestDTO.getPaymentMethods().size() > 0) {
            productRequestDTO.getPaymentMethods().forEach(paymentMethod -> {
                currentProduct.getPaymentMethods().forEach(paymentMethodEntity -> {
                    if (!paymentMethod.equals(paymentMethodEntity.getName())) {
                        currentProduct.setPaymentMethods(findPaymentMethodsByName(productRequestDTO.getPaymentMethods()));
                    }
                });
            });
        }

        return currentProduct
                .withDescription(productRequestDTO.getDescription())
                .withCategoryVariant(categoryVariantService.findByName(productRequestDTO.getVariantName()))
                .withBrand(brandService.findByBrandName(productRequestDTO.getBrandName()))
                .withCategories(findCategoriesByName(productRequestDTO.getCategories()))
                .withPrice(productRequestDTO.getPrice())
                .withProductStyles(findProductStyleByName(productRequestDTO.getProductStyles()))
                .withQuantity(productRequestDTO.getQuantity())
                .withUpdatedAt(Instant.now())
                .withProductApproval(Status.PENDING);
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
