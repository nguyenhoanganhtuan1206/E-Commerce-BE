package com.ecommerce.domain.variant.mapper;

import com.ecommerce.domain.category.CategoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryVariantDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryVariantDTO toCategoryVariantDTO(final CategoryVariantEntity categoryVariant) {
        final CategoryVariantDTO categoryVariantDTO = modelMapper.map(categoryVariant, CategoryVariantDTO.class);

        categoryVariantDTO.setCategory(modelMapper.map(categoryVariant.getCategory(), CategoryDTO.class));
        categoryVariantDTO.setProducts(categoryVariant.getProducts().stream()
                .map(CategoryVariantDTOMapper::apply)
                .collect(Collectors.toSet()));

        return categoryVariantDTO;
    }

    public static Set<CategoryVariantDTO> toCategoryVariantDTOs(final Set<CategoryVariantEntity> categoryVariants) {
        return categoryVariants.stream()
                .map(CategoryVariantDTOMapper::toCategoryVariantDTO)
                .collect(Collectors.toSet());
    }

    private static ProductDTO apply(ProductEntity product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}
