package com.ecommerce.domain.variant;

import com.ecommerce.persistent.variant.CategoryVariantEntity;
import com.ecommerce.persistent.variant.CategoryVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.domain.variant.VariantError.supplyVariantNotFound;
import static com.ecommerce.domain.variant.mapper.CategoryVariantDTOMapper.toCategoryVariantDTOs;

@Service
@RequiredArgsConstructor
public class CategoryVariantService {

    private final CategoryVariantRepository categoryVariantRepository;

    public CategoryVariantEntity findByName(final String name) {
        return categoryVariantRepository.findByVariantName(name)
                .orElseThrow(supplyVariantNotFound("Name", name));
    }

    public List<CategoryVariantDTO> findByCategoryName(final String categoryName) {
        return toCategoryVariantDTOs(categoryVariantRepository.findByCategoryName(categoryName));
    }
}
