package com.ecommerce.domain.user.mapper;

import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.persistent.location.LocationEntity;
import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.seller.SellerEntity;
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

        entity.setLocations(userDTO.getLocations()
                .stream()
                .map(locationDTO -> modelMapper.map(locationDTO, LocationEntity.class))
                .collect(Collectors.toSet()));

        entity.setRoles(userDTO.getRoles()
                .stream()
                .map(roleDTO -> modelMapper.map(roleDTO, RoleEntity.class))
                .collect(Collectors.toSet()));

        if (userDTO.getSeller() != null) {
            entity.setSeller(modelMapper.map(userDTO.getSeller(), SellerEntity.class));
        }

        return entity;
    }

    public static UserDTO toUserDTO(final UserEntity entity) {
        final UserDTO userDTO = modelMapper.map(entity, UserDTO.class);

        userDTO.setLocations(entity.getLocations()
                .stream()
                .map(location -> modelMapper.map(location, LocationDTO.class)).collect(Collectors.toSet()));

        userDTO.setRoles(entity.getRoles()
                .stream()
                .map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toSet()));

        if (entity.getSeller() != null) {
            userDTO.setSeller(modelMapper.map(entity.getSeller(), SellerDTO.class));
        }

        return userDTO;
    }

    public static List<UserDTO> toUserDTOs(final List<UserEntity> entity) {
        return entity.stream().map(UserDTOMapper::toUserDTO).toList();
    }
}
