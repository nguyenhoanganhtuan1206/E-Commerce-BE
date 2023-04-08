package com.ecommerce.domain.product.mapper;

import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO toProductDTO(final ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDTO.class);
    }

    public static Set<ProductDTO> toProductDTOs(final Set<ProductEntity> productEntities) {
        return productEntities.stream().map(ProductDTOMapper::toProductDTO).collect(Collectors.toSet());
    }

    public static ProductEntity toProductEntity(final ProductDTO productDTO) {
        return modelMapper.map(productDTO, ProductEntity.class);
    }
}
