package com.ecommerce.api.category;

import com.ecommerce.api.category.dto.CategoryResponseDTO;
import com.ecommerce.domain.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.ecommerce.api.category.mapper.CategoryDTOMapper.toCategoryResponseDTOs;

@RestController
@RequestMapping(value = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Set<CategoryResponseDTO> findAll() {
        return toCategoryResponseDTOs(categoryService.findAll());
    }
}
