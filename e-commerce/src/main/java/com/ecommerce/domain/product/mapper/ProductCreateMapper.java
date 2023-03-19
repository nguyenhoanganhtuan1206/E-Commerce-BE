package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class ProductCreateMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductEntity toProductEntity(final ProductCreateRequestDTO productCreateRequestDTO) {
        return modelMapper.map(productCreateRequestDTO, ProductEntity.class);
    }

    public static ProductResponseDTO toProductResponseDTO(final ProductEntity entity) {
        return modelMapper.map(entity, ProductResponseDTO.class);
    }
}
