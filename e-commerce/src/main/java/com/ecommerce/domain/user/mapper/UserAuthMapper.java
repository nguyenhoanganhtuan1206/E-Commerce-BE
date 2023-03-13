package com.ecommerce.domain.user.mapper;

import com.ecommerce.api.auth.dto.UserRequestDTO;
import com.ecommerce.api.auth.dto.UserResponseDTO;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;

import static com.ecommerce.domain.location.mapper.LocationEntityMapper.toLocationDTOs;
import static com.ecommerce.domain.location.mapper.LocationEntityMapper.toLocationEntities;

@UtilityClass
public class UserAuthMapper {

    public static UserResponseDTO toUserResponseDTO(final UserEntity userEntity) {
        return UserResponseDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .locations(toLocationDTOs(userEntity.getLocations()))
                .build();
    }

    public static UserResponseDTO toUserResponseDTOWithoutLocations(final UserEntity userEntity) {
        return UserResponseDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .build();
    }


    public static UserEntity toUserEntity(final UserRequestDTO userRequestDTO) {
        return UserEntity.builder()
                .id(userRequestDTO.getId())
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .email(userRequestDTO.getEmail())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .locations(toLocationEntities(userRequestDTO.getLocations()))
                .build();
    }

    public static UserEntity toUserEntityWithOutLocation(final UserRequestDTO userRequestDTO) {
        return UserEntity.builder()
                .id(userRequestDTO.getId())
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .email(userRequestDTO.getEmail())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .build();
    }
}
