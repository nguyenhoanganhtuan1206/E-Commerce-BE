package com.ecommerce.domain.product;

import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.product.ProductError.supplyProductNotFound;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTOs;

@Service
@RequiredArgsConstructor
public class CommonProductService {

    private final ProductRepository productRepository;

    public ProductEntity findById(final UUID id) {
        return productRepository.findById(id)
                .orElseThrow(supplyProductNotFound("id", id));
    }

    public List<ProductEntity> findALlSorted() {
        return productRepository.findALlSorted();
    }

    public List<ProductDTO> findByNameOrSellerName(final String searchTemp) {
        return toProductDTOs(productRepository.findByNameOrSellerName(searchTemp));
    }
}