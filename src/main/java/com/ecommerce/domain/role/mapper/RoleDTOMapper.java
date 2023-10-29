package com.ecommerce.domain.role.mapper;

import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.persistent.role.RoleEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class RoleDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static RoleDTO toRoleDTO(final RoleEntity roleEntity) {
        return modelMapper.map(roleEntity, RoleDTO.class);
    }
}
