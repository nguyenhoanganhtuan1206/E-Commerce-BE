package com.ecommerce.domain.product.mapper;

import com.ecommerce.domain.inventory.InventoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO toProductDTO(final ProductEntity productEntity) {
        final ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);

        productDTO.setSeller(modelMapper.map(productEntity.getSeller(), SellerDTO.class));
        productDTO.setInventory(modelMapper.map(productEntity.getInventory(), InventoryDTO.class));

        return productDTO;
    }

    public static Set<ProductDTO> toProductDTOs(final Set<ProductEntity> productEntities) {
        return productEntities.stream().map(ProductDTOMapper::toProductDTO).collect(Collectors.toSet());
    }

    public static ProductEntity toProductEntity(final ProductDTO productDTO) {
        final ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);

        productEntity.setSeller(modelMapper.map(productDTO.getSeller(), SellerEntity.class));
        productEntity.setInventory(modelMapper.map(productDTO.getInventory(), InventoryEntity.class));

        return productEntity;
    }
}
