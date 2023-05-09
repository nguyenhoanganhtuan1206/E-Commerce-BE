package com.ecommerce.domain.seller.mapper;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class SellerResponseDTOMapper {

    private static final ModelMapper modalMapper = new ModelMapper();

    public static SellerResponseDTO toSellerResponseDTO(final SellerEntity sellerEntity) {
        final SellerResponseDTO sellerResponseDTO = modalMapper.map(sellerEntity, SellerResponseDTO.class);

        sellerResponseDTO.setUserId(sellerEntity.getUser().getId());
        return sellerResponseDTO;
    }

    public static List<SellerResponseDTO> toSellerResponseDTOs(final List<SellerEntity> sellerEntities) {
        return sellerEntities.stream()
                .map(SellerResponseDTOMapper::toSellerResponseDTO)
                .toList();
    }
}
