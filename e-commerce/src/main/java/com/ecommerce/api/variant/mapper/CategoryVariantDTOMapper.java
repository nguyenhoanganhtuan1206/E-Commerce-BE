package com.ecommerce.api.variant.mapper;

import com.ecommerce.api.variant.dto.CategoryVariantResponseDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class CategoryVariantDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryVariantResponseDTO toCategoryVariantResponseDTO(final CategoryVariantDTO categoryVariantDTO) {
        return modelMapper.map(categoryVariantDTO, CategoryVariantResponseDTO.class);
    }

    public static List<CategoryVariantResponseDTO> toCategoryVariantResponseDTOs(final List<CategoryVariantDTO> categoryVariantDTOs) {
        return categoryVariantDTOs.stream()
                .map(CategoryVariantDTOMapper::toCategoryVariantResponseDTO)
                .toList();
    }
}
