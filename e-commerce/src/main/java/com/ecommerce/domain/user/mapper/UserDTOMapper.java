package com.ecommerce.domain.user.mapper;

import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class UserDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserEntity toUserEntity(final UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }

    public static UserDTO toUserDTO(final UserEntity entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    public static List<UserDTO> toUserDTOs(final List<UserEntity> entity) {
        return entity.stream().map(UserDTOMapper::toUserDTO).toList();
    }
}
