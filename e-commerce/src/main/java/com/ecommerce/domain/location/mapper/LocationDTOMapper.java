package com.ecommerce.domain.location.mapper;

import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.persistent.location.LocationEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserEntity;

@UtilityClass
public class LocationDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static LocationEntity toLocationEntity(final LocationDTO locationDTO) {
        final LocationEntity locationEntity = modelMapper.map(locationDTO, LocationEntity.class);

        locationEntity.setUser(toUserEntity(locationDTO.getUserDTO()));
        return modelMapper.map(locationDTO, LocationEntity.class);
    }

    public static LocationDTO toLocationDTO(final LocationEntity locationEntity) {
        final LocationDTO locationDTO = modelMapper.map(locationEntity, LocationDTO.class);

        locationDTO.setUserDTO(toUserDTO(locationEntity.getUser()));
        return locationDTO;
    }
}
