package com.ecommerce.domain.user.mapper;

import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.persistent.location.LocationEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserDTOMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static UserEntity toUserEntity(final UserDTO userDTO) {
        final UserEntity entity = modelMapper.map(userDTO, UserEntity.class);

        entity.setLocations(userDTO.getLocations().stream()
                .map(locationDTO -> modelMapper.map(locationDTO, LocationEntity.class))
                .collect(Collectors.toSet()));

        return entity;
    }

    public static UserDTO toUserDTO(final UserEntity entity) {
        final UserDTO userDTO = modelMapper.map(entity, UserDTO.class);

        entity.setLocations(userDTO.getLocations().stream()
                .map(locationDTO -> modelMapper.map(locationDTO, LocationEntity.class))
                .collect(Collectors.toSet()));

        return userDTO;
    }

    public static List<UserDTO> toUserDTOs(final List<UserEntity> entity) {
        return entity.stream()
                .map(UserDTOMapper::toUserDTO)
                .toList();
    }
}
