package com.ecommerce.domain.location.mapper;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.location.dto.LocationResponseDTO;
import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.persistent.location.LocationEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class LocationDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static LocationEntity toLocationEntity(final LocationDTO locationDTO) {
        final LocationEntity locationEntity = modelMapper.map(locationDTO, LocationEntity.class);

        locationEntity.setUser(modelMapper.map(locationDTO.getUser(), UserEntity.class));
        return locationEntity;
    }

    public static LocationDTO toLocationDTO(final LocationEntity locationEntity) {
        final LocationDTO locationDTO = modelMapper.map(locationEntity, LocationDTO.class);

        locationDTO.setUser(modelMapper.map(locationEntity.getUser(), UserDTO.class));
        return locationDTO;
    }

    public static LocationDTO toLocationDTO(final LocationRequestDTO locationRequestDTO) {
        return modelMapper.map(locationRequestDTO, LocationDTO.class);
    }

    public static LocationResponseDTO toLocationResponseDTO(final LocationDTO locationDTO) {
        return modelMapper.map(locationDTO, LocationResponseDTO.class);
    }
}
