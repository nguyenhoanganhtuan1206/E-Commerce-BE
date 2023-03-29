package com.ecommerce.domain.category.mapper;

import com.ecommerce.domain.brand.BrandDTO;
import com.ecommerce.domain.category.CategoryDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static CategoryDTO toCategoryDTO(final CategoryEntity category) {
        final CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        categoryDTO.setBrands(category.getBrands().stream()
                .map(CategoryDTOMapper::apply)
                .collect(Collectors.toSet()));

        categoryDTO.setCategoryVariants(category.getCategoryVariants().stream()
                .map(CategoryDTOMapper::apply)
                .collect(Collectors.toSet()));

        return modelMapper.map(category, CategoryDTO.class);
    }

    public static Set<CategoryDTO> toCategoryDTOs(final List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(CategoryDTOMapper::toCategoryDTO)
                .collect(Collectors.toSet());
    }

    private static BrandDTO apply(BrandEntity brandEntity) {
        return modelMapper.map(brandEntity, BrandDTO.class);
    }

    private static CategoryVariantDTO apply(CategoryVariantEntity categoryVariantEntity) {
        return modelMapper.map(categoryVariantEntity, CategoryVariantDTO.class);
    }
}
