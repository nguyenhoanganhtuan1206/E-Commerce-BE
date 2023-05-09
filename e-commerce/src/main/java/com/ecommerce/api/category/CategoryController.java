package com.ecommerce.api.category;

import com.ecommerce.api.category.dto.CategoryResponseDTO;
import com.ecommerce.domain.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ecommerce.domain.category.mapper.CategoryResponseDTOMapper.toCategoryResponseDTOs;

@RestController
@RequestMapping(value = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> findAll() {
        return toCategoryResponseDTOs(categoryService.findAll());
    }
}
