package com.ecommerce.api.brand;

import com.ecommerce.api.brand.dto.BrandResponseDTO;
import com.ecommerce.domain.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ecommerce.api.brand.mapper.BrandDTOMapper.toBrandResponseDTOs;

@RestController
@RequestMapping(value = "/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public List<BrandResponseDTO> findByCategoryName(final @RequestParam String categoryName) {
        return toBrandResponseDTOs(brandService.findByCategoryName(categoryName));
    }
}
