package com.ecommerce.domain.style;

import com.ecommerce.persistent.style.ProductStyleEntity;
import com.ecommerce.persistent.style.ProductStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.domain.style.ProductStyleError.supplyProductStyleExisted;

@Service
@RequiredArgsConstructor
public class ProductStyleService {

    private final ProductStyleRepository productStyleRepository;

    public List<ProductStyleEntity> findAll() {
        return productStyleRepository.findAll();
    }

    public ProductStyleEntity createProductStyle(final ProductStyleEntity productStyle) {
        verifyIfProductStyleAvailable(productStyle.getName());

        return productStyleRepository.save(productStyle);
    }

    private void verifyIfProductStyleAvailable(final String productStyleName) {
        productStyleRepository.findByName(productStyleName)
                .ifPresent(productStyleEntity -> {
                    throw supplyProductStyleExisted("name", productStyleName).get();
                });
    }
}
