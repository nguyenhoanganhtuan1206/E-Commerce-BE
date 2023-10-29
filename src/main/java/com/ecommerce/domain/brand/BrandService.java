package com.ecommerce.domain.brand;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.domain.brand.BrandError.supplyBrandNotFound;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandEntity> findAll() {
        return brandRepository.findAll();
    }

    public BrandEntity findByBrandName(final String name) {
        return brandRepository.findByBrandName(name)
                .orElseThrow(supplyBrandNotFound("name", name));
    }
}
