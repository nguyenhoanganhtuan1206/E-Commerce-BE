package com.ecommerce.domain.location.mapper;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.location.dto.LocationResponseDTO;
import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.persistent.location.LocationEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.List;

@UtilityClass
public class LocationDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static LocationEntity toLocationEntity(final LocationDTO locationDTO) {
        return modelMapper.map(locationDTO, LocationEntity.class);
    }

    public static LocationDTO toLocationDTO(final LocationEntity locationEntity) {
        return modelMapper.map(locationEntity, LocationDTO.class);
    }

    public static List<LocationDTO> toLocationDTOs(final List<LocationEntity> locationEntities) {
        return locationEntities.stream()
                .map(LocationDTOMapper::toLocationDTO)
                .toList();
    }

    public static LocationDTO toLocationDTO(final LocationRequestDTO locationRequestDTO) {
        return modelMapper.map(locationRequestDTO, LocationDTO.class);
    }

    public static LocationResponseDTO toLocationResponseDTO(final LocationDTO locationDTO) {
        final LocationResponseDTO locationResponseDTO = modelMapper.map(locationDTO, LocationResponseDTO.class);
        locationResponseDTO.setUserId(locationDTO.getUser().getId());

        return locationResponseDTO;
    }

    public static List<LocationResponseDTO> toLocationResponseDTOs(final List<LocationDTO> locationDTOs) {
        return locationDTOs.stream()
                .map(LocationDTOMapper::toLocationResponseDTO)
                .toList();
    }
}
