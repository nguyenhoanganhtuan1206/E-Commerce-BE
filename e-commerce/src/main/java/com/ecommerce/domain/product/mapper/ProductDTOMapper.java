package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO toProductDTO(final ProductEntity productEntity) {
        final ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);

        productDTO.setCategory(modelMapper.map(productEntity.getCategory(), CategoryDTO.class));
        productDTO.setSeller(modelMapper.map(productEntity.getSeller(), SellerDTO.class));

        return productDTO;
    }

    public static Set<ProductDTO> toProductDTOs(final Set<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductDTOMapper::toProductDTO)
                .collect(Collectors.toSet());
    }

    public static ProductEntity toProductEntity(final ProductDTO productDTO) {
        final ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);

        productEntity.setSeller(modelMapper.map(productDTO.getSeller(), SellerEntity.class));
        productEntity.setCategory(modelMapper.map(productDTO.getCategory(), CategoryEntity.class));

        return productEntity;
    }
}
