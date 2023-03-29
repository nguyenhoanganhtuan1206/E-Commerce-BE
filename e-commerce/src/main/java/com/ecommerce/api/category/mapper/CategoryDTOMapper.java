package com.ecommerce.api.category.mapper;

import com.ecommerce.api.category.dto.CategoryResponseDTO;
import com.ecommerce.domain.category.CategoryDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryResponseDTO toCategoryResponseDTO(final CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, CategoryResponseDTO.class);
    }

    public static Set<CategoryResponseDTO> toCategoryResponseDTOs(final Set<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream()
                .map(CategoryDTOMapper::toCategoryResponseDTO)
                .collect(Collectors.toSet());
    }
}
