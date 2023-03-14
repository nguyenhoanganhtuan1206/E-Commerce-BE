package com.ecommerce.domain.user.mapper;

import com.ecommerce.api.auth.dto.UserAuthRequestDTO;
import com.ecommerce.api.auth.dto.UserAuthResponseDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserAuthMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static UserAuthResponseDTO toUserResponseDTO(final UserEntity userEntity) {
        return modelMapper.map(userEntity, UserAuthResponseDTO.class);
    }

    public static UserEntity toUserEntity(final UserAuthRequestDTO authRequestDTO) {
        return modelMapper.map(authRequestDTO, UserEntity.class);
    }
}
