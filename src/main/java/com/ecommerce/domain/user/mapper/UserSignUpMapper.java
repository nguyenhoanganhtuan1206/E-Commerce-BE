package com.ecommerce.domain.user.mapper;

import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserSignUpMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserSignUpResponseDTO toUserSignUpResponseDTO(final UserEntity userEntity) {
        return modelMapper.map(userEntity, UserSignUpResponseDTO.class);
    }
}
