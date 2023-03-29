package com.ecommerce.domain.category;

import com.ecommerce.persistent.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.category.CategoryError.supplyCategoryNotFound;
import static com.ecommerce.domain.category.mapper.CategoryDTOMapper.toCategoryDTO;
import static com.ecommerce.domain.category.mapper.CategoryDTOMapper.toCategoryDTOs;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Set<CategoryDTO> findAll() {
        return toCategoryDTOs(categoryRepository.findAll());
    }

    public CategoryDTO findById(final UUID id) {
        return toCategoryDTO(categoryRepository.findById(id)
                .orElseThrow(supplyCategoryNotFound(id)));
    }
}
