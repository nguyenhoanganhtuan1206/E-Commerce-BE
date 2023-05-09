package com.ecommerce.domain.brand;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandEntity> findAll() {
        return brandRepository.findAll();
    }
}
