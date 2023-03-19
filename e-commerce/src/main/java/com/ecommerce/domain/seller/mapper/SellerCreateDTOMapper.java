package com.ecommerce.domain.seller.mapper;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class SellerCreateDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static SellerEntity toSellerEntity(final SellerCreateRequestDTO sellerCreateRequestDTO) {
        final SellerEntity sellerEntity = modelMapper.map(sellerCreateRequestDTO, SellerEntity.class);
        final UserEntity userEntity = modelMapper.map(sellerCreateRequestDTO.getUser(), UserEntity.class);
        sellerEntity.setUser(userEntity);

        return sellerEntity;
    }
}
