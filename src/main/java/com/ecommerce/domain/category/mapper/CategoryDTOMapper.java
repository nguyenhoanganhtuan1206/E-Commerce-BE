package com.ecommerce.domain.category.mapper;

import com.ecommerce.domain.category.CategoryDTO;
import com.ecommerce.persistent.category.CategoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static CategoryDTO toCategoryDTO(final CategoryEntity category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public static Set<CategoryDTO> toCategoryDTOs(final List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(CategoryDTOMapper::toCategoryDTO)
                .collect(Collectors.toSet());
    }
}
