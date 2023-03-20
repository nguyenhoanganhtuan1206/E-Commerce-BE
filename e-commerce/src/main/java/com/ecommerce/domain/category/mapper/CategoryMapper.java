package com.ecommerce.domain.category.mapper;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.persistent.category.CategoryEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class CategoryMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static CategoryDTO toCategoryDTO(final CategoryEntity category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}
