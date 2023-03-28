package com.ecommerce.api.user.mapper;

import com.ecommerce.api.user.dto.UserResponseDTO;
import com.ecommerce.domain.user.UserDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class UserResponseDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public static UserResponseDTO toUserResponseDTO(final UserDTO userDTO) {
        return modelMapper.map(userDTO, UserResponseDTO.class);
    }
}
