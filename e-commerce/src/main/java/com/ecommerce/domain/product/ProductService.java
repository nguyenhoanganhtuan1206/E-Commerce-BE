package com.ecommerce.domain.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.product.mapper.ProductDTOMapper;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.seller.SellerService;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.product.ProductError.supplyProductExisted;
import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.*;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTOs;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductEntity;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final SellerService sellerService;

    public Set<ProductResponseDTO> findBySellerId(final UUID sellerId) {
        return toProductResponseDTOs(productRepository.findBySellerId(sellerId));
    }

    public Set<ProductDTO> findByNameOrSellerName(final String searchTemp) {
        return toProductDTOs(productRepository.findByNameOrSellerName(searchTemp));
    }

    public ProductResponseDTO create(final UUID sellerId, final ProductCreateRequestDTO productCreateRequestDTO) {
        verifyIfProductAvailable(productCreateRequestDTO.getName());

        final SellerDTO sellerDTO = sellerService.findById(sellerId);
        final ProductDTO product = toProductDTO(productCreateRequestDTO);
        product.setSeller(sellerDTO);

        return toProductResponseDTO(productRepository.save(toProductEntity(product)));
    }

    private void verifyIfProductAvailable(final String name) {
        Optional<ProductDTO> product = productRepository.findByNameContainingIgnoreCase(name).map(ProductDTOMapper::toProductDTO);

        if (product.isPresent()) {
            throw supplyProductExisted(name).get();
        }
    }
}
