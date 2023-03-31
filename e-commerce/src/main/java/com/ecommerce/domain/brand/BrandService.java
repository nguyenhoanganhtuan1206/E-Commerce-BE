package com.ecommerce.domain.brand;

import com.ecommerce.persistent.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.ecommerce.domain.brand.mapper.BrandDTOMapper.toBrandDTOs;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public Set<BrandDTO> findByCategoryName(final String categoryName) {
        return toBrandDTOs(brandRepository.findByCategoryName(categoryName));
    }
}
