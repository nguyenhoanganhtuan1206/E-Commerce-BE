package com.ecommerce.domain.seller.mapper;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.seller.SellerDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class SellerCreateDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static SellerDTO toSellerDTO(final SellerCreateRequestDTO sellerCreateRequestDTO) {
        return modelMapper.map(sellerCreateRequestDTO, SellerDTO.class);
    }
}
