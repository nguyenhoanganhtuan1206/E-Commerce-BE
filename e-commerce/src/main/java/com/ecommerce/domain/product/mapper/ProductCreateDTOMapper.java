package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductCreateDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO toProductDTO(final ProductCreateRequestDTO productDTO) {
        return modelMapper.map(productDTO, ProductDTO.class);
    }

    public static ProductResponseDTO toProductResponseDTO(final ProductEntity entity) {
        final ProductResponseDTO productDTO = modelMapper.map(entity, ProductResponseDTO.class);

        return productDTO;
    }

    public static Set<ProductResponseDTO> toProductResponseDTOs(final Set<ProductEntity> productEntities) {
        return productEntities.stream().map(ProductCreateDTOMapper::toProductResponseDTO).collect(Collectors.toSet());
    }
}
