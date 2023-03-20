package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductCreateDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductEntity toProductEntity(final ProductCreateRequestDTO productDTO) {
        final ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);

        productEntity.setSeller(modelMapper.map(productDTO.getSeller(), SellerEntity.class));
        productEntity.setCategory(modelMapper.map(productDTO.getCategoryDTO(), CategoryEntity.class));

        return productEntity;
    }

    public static ProductResponseDTO toProductResponseDTO(final ProductEntity entity) {
        final ProductResponseDTO productDTO = modelMapper.map(entity, ProductResponseDTO.class);

        productDTO.setCategory(modelMapper.map(entity.getCategory(), CategoryDTO.class));

        return productDTO;
    }

    public static Set<ProductResponseDTO> toProductResponseDTOs(final Set<ProductEntity> productEntities) {
        return productEntities.stream().map(ProductCreateDTOMapper::toProductResponseDTO).collect(Collectors.toSet());
    }
}
