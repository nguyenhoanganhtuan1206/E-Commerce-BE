package com.ecommerce.domain.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.category.CategoryService;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.seller.SellerService;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.product.mapper.ProductCreateDTOMapper.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    private final SellerService sellerService;

    public Set<ProductResponseDTO> findBySellerId(final UUID sellerId) {
        return toProductResponseDTOs(productRepository.findBySellerId(sellerId));
    }

    public ProductResponseDTO create(
            final UUID sellerId,
            final ProductCreateRequestDTO productDTO
    ) {
        final SellerDTO sellerDTO = sellerService.findById(sellerId);

        productDTO.setSeller(sellerDTO);
        productDTO.setCategoryDTO(categoryService.findById(productDTO.getCategoryId()));

        return toProductResponseDTO(productRepository.save(toProductEntity(productDTO)));
    }
}
