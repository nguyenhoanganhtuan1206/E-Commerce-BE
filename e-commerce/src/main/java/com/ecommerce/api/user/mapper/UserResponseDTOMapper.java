package com.ecommerce.api.user.mapper;

import com.ecommerce.api.user.dto.UserResponseDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserResponseDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserResponseDTO toUserResponseDTO(final UserEntity userEntity) {
        return modelMapper.map(userEntity, UserResponseDTO.class);
    }
}
