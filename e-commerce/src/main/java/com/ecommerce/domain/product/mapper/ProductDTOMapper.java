package com.ecommerce.domain.product.mapper;

import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class ProductDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO toProductDTO(final ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDTO.class);
    }

    public static List<ProductDTO> toProductDTOs(final List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductDTOMapper::toProductDTO)
                .toList();
    }
}
