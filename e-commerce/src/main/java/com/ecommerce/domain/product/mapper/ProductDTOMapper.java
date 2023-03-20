package com.ecommerce.domain.product.mapper;

import com.ecommerce.api.category.CategoryDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.product.ProductEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class ProductDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO toProductDTO(final ProductEntity productEntity) {
        final ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);

        productDTO.setCategory(modelMapper.map(productEntity.getCategory(), CategoryDTO.class));
        productDTO.setSeller(modelMapper.map(productEntity.getSeller(), SellerDTO.class));

        return productDTO;
    }
}
