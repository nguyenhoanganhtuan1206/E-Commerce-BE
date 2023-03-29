package com.ecommerce.api.variant;

import com.ecommerce.api.variant.dto.CategoryVariantResponseDTO;
import com.ecommerce.domain.variant.CategoryVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.ecommerce.api.variant.mapper.CategoryVariantDTOMapper.toCategoryVariantResponseDTOs;

@RestController
@RequestMapping(value = "/api/v1/category-variant")
@RequiredArgsConstructor
public class CategoryVariantController {

    private final CategoryVariantService categoryVariantService;

    @GetMapping
    public Set<CategoryVariantResponseDTO> find(final @RequestParam String categoryName) {
        return toCategoryVariantResponseDTOs(categoryVariantService.findByCategoryName(categoryName));
    }
}
