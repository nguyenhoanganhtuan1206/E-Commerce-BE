package com.ecommerce.domain.variant;

import com.ecommerce.persistent.variant.CategoryVariantEntity;
import com.ecommerce.persistent.variant.CategoryVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.domain.variant.VariantError.supplyVariantNotFound;

@Service
@RequiredArgsConstructor
public class CategoryVariantService {

    private final CategoryVariantRepository categoryVariantRepository;

    public List<CategoryVariantEntity> findAll() {
        return categoryVariantRepository.findAll();
    }

    public CategoryVariantEntity findByName(final String name) {
        return categoryVariantRepository.findByName(name)
                .orElseThrow(supplyVariantNotFound("name", name));
    }
}
