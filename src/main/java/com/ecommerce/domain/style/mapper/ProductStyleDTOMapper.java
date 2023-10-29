package com.ecommerce.domain.style.mapper;

import com.ecommerce.api.style.dto.ProductStyleRequestDTO;
import com.ecommerce.api.style.dto.ProductStyleResponseDTO;
import com.ecommerce.persistent.style.ProductStyleEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class ProductStyleDTOMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductStyleEntity toProductStyleEntity(final ProductStyleRequestDTO productStyleRequestDTO) {
        return modelMapper.map(productStyleRequestDTO, ProductStyleEntity.class);
    }

    public static ProductStyleResponseDTO toProductStyleResponseDTO(final ProductStyleEntity productStyleEntity) {
        return modelMapper.map(productStyleEntity, ProductStyleResponseDTO.class);
    }

    public static List<ProductStyleResponseDTO> toProductStyleResponseDTOs(final List<ProductStyleEntity> productStyleEntities) {
        return productStyleEntities.stream()
                .map(ProductStyleDTOMapper::toProductStyleResponseDTO)
                .toList();
    }
}
