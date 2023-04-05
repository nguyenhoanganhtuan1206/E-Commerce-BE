package com.ecommerce.domain.category;

import com.ecommerce.persistent.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.ecommerce.domain.category.mapper.CategoryDTOMapper.toCategoryDTOs;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Set<CategoryDTO> findAll() {
        return toCategoryDTOs(categoryRepository.findAll());
    }
}
