package com.ecommerce.api.seller.mapper;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.domain.seller.SellerDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class SellerResponseDTOMapper {

    private static final ModelMapper modalMapper = new ModelMapper();

    public static SellerResponseDTO toSellerResponseDTO(final SellerDTO sellerDTO) {
        return modalMapper.map(sellerDTO, SellerResponseDTO.class);
    }

    public static List<SellerResponseDTO> toSellerResponseDTOs(final List<SellerDTO> sellerDTOs) {
        return sellerDTOs.stream()
                .map(SellerResponseDTOMapper::toSellerResponseDTO)
                .toList();
    }
}
