package com.ecommerce.domain.seller.mapper;

import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class SellerDTOMapper {

    private static final ModelMapper modalMapper = new ModelMapper();

    public static SellerDTO toSellerDTO(final SellerEntity seller) {
        return modalMapper.map(seller, SellerDTO.class);
    }
}
