package com.ecommerce.api.seller.mapper;

import com.ecommerce.api.seller.dto.SellerSignUpResponseDTO;
import com.ecommerce.domain.seller.SellerDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class SellerSignUpDTOMapper {

    private static final ModelMapper modalMapper = new ModelMapper();

    public static SellerSignUpResponseDTO toSellerSignUpResponseDTO(final SellerDTO sellerDTO) {
        return modalMapper.map(sellerDTO, SellerSignUpResponseDTO.class);
    }
}
