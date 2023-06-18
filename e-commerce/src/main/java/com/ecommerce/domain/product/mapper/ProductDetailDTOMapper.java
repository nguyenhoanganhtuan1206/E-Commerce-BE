package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.product.dto.ProductDetailsDTO;
import com.ecommerce.api.product.dto.ProductResponseDetailDTO;
import com.ecommerce.domain.inventory.dto.InventoryResponseDTO;
import com.ecommerce.domain.inventory.mapper.InventoryDTOMapper;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.style.ProductStyleEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class ProductDetailDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductResponseDetailDTO toProductResponseDetailDTO(final ProductEntity entity) {
        final ProductResponseDetailDTO productResponseDetailDTO = modelMapper.map(entity, ProductResponseDetailDTO.class);

        productResponseDetailDTO.setBrandName(entity.getBrand().getBrandName());
        productResponseDetailDTO.setVariantName(entity.getCategoryVariant().getName());

        final List<String> categoryNames = entity.getCategories().stream()
                .map(CategoryEntity::getCategoryName)
                .toList();
        productResponseDetailDTO.setCategories(categoryNames);

        final List<String> paymentMethodsName = entity.getPaymentMethods().stream()
                .map(PaymentMethodEntity::getName)
                .toList();
        productResponseDetailDTO.setPaymentMethods(paymentMethodsName);

        final List<String> productStylesName = entity.getProductStyles().stream()
                .map(ProductStyleEntity::getName)
                .toList();
        productResponseDetailDTO.setProductStyles(productStylesName);

        if (entity.getInventories() != null) {
            final List<InventoryResponseDTO> inventories = entity.getInventories().stream()
                    .map(InventoryDTOMapper::toInventoryResponseDTO)
                    .toList();
            productResponseDetailDTO.setInventories(inventories);
        }

        return productResponseDetailDTO;
    }

    public static List<ProductResponseDetailDTO> toProductsResponseDetailDTOs(final List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductDetailDTOMapper::toProductResponseDetailDTO)
                .toList();
    }

    public static ProductDetailsDTO toProductDetailsDTO(final ProductEntity productEntity) {
        final ProductDetailsDTO productDetailsDTO = modelMapper.map(productEntity, ProductDetailsDTO.class);

        productDetailsDTO.setBrandName(productEntity.getBrand().getBrandName());
        productDetailsDTO.setVariantName(productEntity.getCategoryVariant().getName());

        final List<String> categoryNames = productEntity.getCategories().stream()
                .map(CategoryEntity::getCategoryName)
                .toList();
        productDetailsDTO.setCategories(categoryNames);

        final List<String> paymentMethodsName = productEntity.getPaymentMethods().stream()
                .map(PaymentMethodEntity::getName)
                .toList();
        productDetailsDTO.setPaymentMethods(paymentMethodsName);

        final List<String> productStylesName = productEntity.getProductStyles().stream()
                .map(ProductStyleEntity::getName)
                .toList();
        productDetailsDTO.setProductStyles(productStylesName);

        return productDetailsDTO;
    }
}
