package com.ecommerce.domain.seller.mapper;

import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class SellerDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static SellerDTO toSellerDTO(final SellerEntity sellerEntity) {
        return modelMapper.map(sellerEntity, SellerDTO.class);
    }

    public static SellerEntity toSellerEntity(final SellerDTO sellerDTO) {
        return modelMapper.map(sellerDTO, SellerEntity.class);
    }

    public static List<SellerDTO> toSellerDTOs(final List<SellerEntity> sellerEntities) {
        return sellerEntities.stream()
                .map(SellerDTOMapper::toSellerDTO)
                .toList();
    }
}
