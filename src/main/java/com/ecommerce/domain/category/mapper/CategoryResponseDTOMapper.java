package com.ecommerce.domain.category.mapper;

import com.ecommerce.api.category.dto.CategoryResponseDTO;
import com.ecommerce.persistent.category.CategoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class CategoryResponseDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryResponseDTO toCategoryResponseDTO(final CategoryEntity category) {
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public static List<CategoryResponseDTO> toCategoryResponseDTOs(final List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(CategoryResponseDTOMapper::toCategoryResponseDTO)
                .toList();
    }
}
