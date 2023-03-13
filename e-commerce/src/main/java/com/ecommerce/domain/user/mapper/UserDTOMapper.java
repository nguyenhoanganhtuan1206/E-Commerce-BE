package com.ecommerce.domain.user.mapper;

import com.ecommerce.api.user.dto.UserDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.ecommerce.domain.location.mapper.LocationEntityMapper.toLocationDTOs;
import static com.ecommerce.domain.location.mapper.LocationEntityMapper.toLocationEntities;

@UtilityClass
public class UserDTOMapper {

    public static UserDTO toUserDTO(final UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .locations(toLocationDTOs(userEntity.getLocations()))
                .build();
    }

    public static List<UserDTO> toUserDTOs(final List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(UserDTOMapper::toUserDTO)
                .toList();
    }

    public static UserEntity toUserEntity(final UserDTO userDTO) {
        return UserEntity.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .locations(toLocationEntities(userDTO.getLocations()))
                .build();
    }
}
