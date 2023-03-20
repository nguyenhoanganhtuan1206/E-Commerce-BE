package com.ecommerce.domain.category;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.persistent.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ecommerce.domain.category.CategoryError.supplyCategoryNotFound;
import static com.ecommerce.domain.category.mapper.CategoryMapper.toCategoryDTO;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDTO findById(final UUID id) {
        return toCategoryDTO(categoryRepository.findById(id)
                .orElseThrow(supplyCategoryNotFound(id)));
    }
}
