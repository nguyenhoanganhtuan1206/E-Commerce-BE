package com.ecommerce.domain.user.mapper;

import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserSignUpMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static UserSignUpResponseDTO toUserResponseDTO(final UserEntity userEntity) {
        return modelMapper.map(userEntity, UserSignUpResponseDTO.class);
    }

    public static UserEntity toUserEntity(final UserSignUpRequestDTO authRequestDTO) {
        final UserEntity userEntity = modelMapper.map(authRequestDTO, UserEntity.class);

        userEntity.setRole(modelMapper.map(authRequestDTO.getRoleDTO(), RoleEntity.class));

        return modelMapper.map(authRequestDTO, UserEntity.class);
    }
}
