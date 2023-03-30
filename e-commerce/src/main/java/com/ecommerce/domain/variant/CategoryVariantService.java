package com.ecommerce.domain.variant;

import com.ecommerce.persistent.variant.CategoryVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.ecommerce.domain.variant.mapper.CategoryVariantDTOMapper.toCategoryVariantDTOs;

@Service
@RequiredArgsConstructor
public class CategoryVariantService {

    private final CategoryVariantRepository categoryVariantRepository;

    public Set<CategoryVariantDTO> findByCategoryName(final String categoryName) {
        return toCategoryVariantDTOs(categoryVariantRepository.findByCategoryName(categoryName));
    }
}
