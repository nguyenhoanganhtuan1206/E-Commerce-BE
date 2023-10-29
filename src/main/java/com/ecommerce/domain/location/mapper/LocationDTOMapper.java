package com.ecommerce.domain.location.mapper;

import com.ecommerce.api.location.dto.LocationResponseDTO;
import com.ecommerce.persistent.location.LocationEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class LocationDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static LocationResponseDTO toLocationResponseDTO(final LocationEntity locationEntity) {
        final LocationResponseDTO locationResponseDTO = modelMapper.map(locationEntity, LocationResponseDTO.class);
        locationResponseDTO.setUserId(locationEntity.getUser().getId());

        return locationResponseDTO;
    }

    public static List<LocationResponseDTO> toLocationResponseDTOs(final List<LocationEntity> locationEntities) {
        return locationEntities.stream()
                .map(LocationDTOMapper::toLocationResponseDTO)
                .toList();
    }
}
