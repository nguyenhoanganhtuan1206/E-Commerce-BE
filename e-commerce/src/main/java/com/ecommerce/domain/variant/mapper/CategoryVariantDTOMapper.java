package com.ecommerce.domain.variant.mapper;

import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class CategoryVariantDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryVariantDTO toCategoryVariantDTO(final CategoryVariantEntity categoryVariant) {
        return modelMapper.map(categoryVariant, CategoryVariantDTO.class);
    }

    public static List<CategoryVariantDTO> toCategoryVariantDTOs(final List<CategoryVariantEntity> categoryVariants) {
        return categoryVariants.stream()
                .map(CategoryVariantDTOMapper::toCategoryVariantDTO)
                .toList();
    }
}
