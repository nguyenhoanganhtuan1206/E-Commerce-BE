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

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserEntity toUserEntity(final UserDTO userDTO) {
        final UserEntity entity = modelMapper.map(userDTO, UserEntity.class);

        if (userDTO.getLocations() != null) {
            entity.setLocations(userDTO.getLocations()
                    .stream()
                    .map(UserDTOMapper::apply)
                    .collect(Collectors.toSet()));
        }

        entity.setRoles(userDTO.getRoles()
                .stream()
                .map(UserDTOMapper::apply)
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
                .map(UserDTOMapper::apply)
                .collect(Collectors.toSet()));

        userDTO.setRoles(entity.getRoles()
                .stream()
                .map(UserDTOMapper::apply)
                .collect(Collectors.toSet()));

        if (entity.getSeller() != null) {
            userDTO.setSeller(modelMapper.map(entity.getSeller(), SellerDTO.class));
        }

        return userDTO;
    }

    public static List<UserDTO> toUserDTOs(final List<UserEntity> entity) {
        return entity.stream().map(UserDTOMapper::toUserDTO).toList();
    }

    private static LocationEntity apply(LocationDTO locationDTO) {
        return modelMapper.map(locationDTO, LocationEntity.class);
    }

    private static RoleDTO apply(RoleEntity role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    private static RoleEntity apply(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, RoleEntity.class);
    }

    private static LocationDTO apply(LocationEntity location) {
        return modelMapper.map(location, LocationDTO.class);
    }
}
