package com.ecommerce.domain.product;

import com.ecommerce.api.product.dto.ProductDetailsDTO;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.inventory.dto.InventoryDetailResponseDTO;
import com.ecommerce.domain.product.dto.ProductDTO;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.product.ProductError.supplyProductNotFound;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTOs;
import static com.ecommerce.domain.product.mapper.ProductDetailDTOMapper.toProductDetailsDTO;

@Service
@RequiredArgsConstructor
public class CommonProductService {

    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    public ProductEntity findById(final UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(supplyProductNotFound("id", productId));
    }

    public ProductDetailsDTO findProductDetailById(final UUID productId) {
        final ProductDetailsDTO productDetailDTO = toProductDetailsDTO(productRepository.findById(productId)
                .orElseThrow(supplyProductNotFound("id", productId)));
        final InventoryDetailResponseDTO inventoryEntities = inventoryService.findInventoryDetailByProductId(productId);

        if (inventoryEntities != null) {
            return productDetailDTO
                    .withInventory(inventoryEntities);
        }

        return productDetailDTO;
    }

    public List<ProductEntity> findAllSortedByAmountSoldOut() {
        return productRepository.findAllSortedByAmountSoldOut();
    }

    public List<ProductEntity> findALlSorted() {
        return productRepository.findALlSorted();
    }

    public List<ProductDTO> findByNameOrSellerName(final String searchTemp) {
        return toProductDTOs(productRepository.findByNameOrSellerName(searchTemp));
    }
}
