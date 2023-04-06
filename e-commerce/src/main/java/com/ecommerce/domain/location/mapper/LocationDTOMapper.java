package com.ecommerce.domain.location.mapper;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.location.dto.LocationResponseDTO;
import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.persistent.location.LocationEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class LocationDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static LocationEntity toLocationEntity(final LocationDTO locationDTO) {
        return modelMapper.map(locationDTO, LocationEntity.class);
    }

    public static LocationDTO toLocationDTO(final LocationEntity locationEntity) {
        return modelMapper.map(locationEntity, LocationDTO.class);
    }

    public static Set<LocationDTO> toLocationDTOs(final Set<LocationEntity> locationEntities) {
        return locationEntities.stream()
                .map(LocationDTOMapper::toLocationDTO)
                .collect(Collectors.toSet());
    }

    public static LocationDTO toLocationDTO(final LocationRequestDTO locationRequestDTO) {
        return modelMapper.map(locationRequestDTO, LocationDTO.class);
    }

    public static LocationResponseDTO toLocationResponseDTO(final LocationDTO locationDTO) {
        return modelMapper.map(locationDTO, LocationResponseDTO.class);
    }
}
