package com.ecommerce.domain.user.mapper;

import com.ecommerce.api.user.dto.UserUpdateResponseDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserUpdateMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static UserUpdateResponseDTO toUserUpdateDTO(final UserEntity userEntity) {
        return modelMapper.map(userEntity, UserUpdateResponseDTO.class);
    }
}
