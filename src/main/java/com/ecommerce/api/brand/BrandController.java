package com.ecommerce.api.brand;

import com.ecommerce.domain.brand.BrandDTO;
import com.ecommerce.domain.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ecommerce.domain.brand.mapper.BrandDTOMapper.toBrandDTOs;

@RestController
@RequestMapping(value = "/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public List<BrandDTO> findAll() {
        return toBrandDTOs(brandService.findAll());
    }
}
