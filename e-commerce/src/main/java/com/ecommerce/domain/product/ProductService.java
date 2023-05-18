package com.ecommerce.domain.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
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
import static com.ecommerce.domain.product.ProductError.supplyProductExisted;
import static com.ecommerce.domain.product.ProductVariation.*;
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

    public List<ProductResponseDTO> findAll() {
        return toProductResponseDTOs(productRepository.findAll());
    }

    public List<ProductResponseDTO> findBySellerId(final UUID sellerId) {
        return toProductResponseDTOs(productRepository.findBySellerId(sellerId));
    }

    public List<ProductDTO> findByNameOrSellerName(final String searchTemp) {
        return toProductDTOs(productRepository.findByNameOrSellerName(searchTemp));
    }

    public void create(final ProductCreateRequestDTO productRequestDTO) {
        final SellerEntity seller = sellerService.findByUserId(authsProvider.getCurrentUserId());
        validatePaymentMethodNotEmpty(productRequestDTO.getPaymentMethods());
        validateCategoriesNotEmpty(productRequestDTO.getCategories());
        validateProductStylesNotEmpty(productRequestDTO.getProductStyles());

        verifyIfProductAvailable(productRequestDTO.getName());

        final ProductEntity productCreate = buildProductEntity(productRequestDTO, seller);
        if (productRequestDTO.getInventories().isEmpty()) {
            validatePriceProduct(productRequestDTO.getPrice());
            inventoryService.createInventories(toInventoryEntities(productRequestDTO.getInventories()), productRepository.save(productCreate));
        } else {
            productRepository.save(productCreate);
        }
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
