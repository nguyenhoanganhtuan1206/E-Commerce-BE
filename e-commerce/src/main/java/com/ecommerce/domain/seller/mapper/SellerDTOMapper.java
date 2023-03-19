package com.ecommerce.domain.seller.mapper;

import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class SellerDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static SellerDTO toSellerDTO(final SellerEntity sellerEntity) {
        final SellerDTO sellerDTO = modelMapper.map(sellerEntity, SellerDTO.class);
        final UserDTO userDTO = modelMapper.map(sellerEntity.getUser(), UserDTO.class);

        sellerDTO.setUser(userDTO);
        return sellerDTO;
    }

    public static SellerEntity toSellerEntity(final SellerDTO sellerDTO) {
        final SellerEntity sellerEntity = modelMapper.map(sellerDTO, SellerEntity.class);
        final UserEntity userEntity = modelMapper.map(sellerDTO.getUser(), UserEntity.class);

        sellerEntity.setUser(userEntity);
        return sellerEntity;
    }
}
