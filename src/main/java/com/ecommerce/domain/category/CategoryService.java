package com.ecommerce.domain.category;

import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.domain.category.CategoryError.supplyCategoryNotFound;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryEntity findByCategoryName(final String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(supplyCategoryNotFound(categoryName));
    }

    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }
}
