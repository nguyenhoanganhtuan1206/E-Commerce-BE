package com.ecommerce.api.variant;

import com.ecommerce.domain.variant.CategoryVariantDTO;
import com.ecommerce.domain.variant.CategoryVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ecommerce.domain.variant.mapper.CategoryVariantDTOMapper.toCategoryVariantDTOs;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class CategoryVariantController {

    private final CategoryVariantService categoryVariantService;

    @GetMapping
    public List<CategoryVariantDTO> findAll() {
        return toCategoryVariantDTOs(categoryVariantService.findAll());
    }
}
