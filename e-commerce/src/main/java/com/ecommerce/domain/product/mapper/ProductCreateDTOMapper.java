package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class ProductCreateDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductResponseDTO toProductResponseDTO(final ProductEntity entity) {
        final ProductResponseDTO productDTO = modelMapper.map(entity, ProductResponseDTO.class);

        productDTO.setSellerId(entity.getSeller().getId());
        return productDTO;
    }

    public static List<ProductResponseDTO> toProductResponseDTOs(final List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductCreateDTOMapper::toProductResponseDTO)
                .toList();
    }
}
