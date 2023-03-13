package com.ecommerce.domain.location.mapper;

import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.persistent.location.LocationEntity;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserEntity;

@UtilityClass
public class LocationEntityMapper {

    public static LocationEntity toLocationEntity(final LocationDTO locationDTO) {
        return LocationEntity.builder()
                .id(locationDTO.getId())
                .address(locationDTO.getAddress())
                .city(locationDTO.getCity())
                .district(locationDTO.getDistrict())
                .commune(locationDTO.getCommune())
                .user(toUserEntity(locationDTO.getUserDTO()))
                .build();
    }

    public static Set<LocationEntity> toLocationEntities(final Set<LocationDTO> locationDTOs) {
        return locationDTOs.stream()
                .map(LocationEntityMapper::toLocationEntity)
                .collect(Collectors.toSet());
    }

    public static LocationDTO toLocationDTO(final LocationEntity locationEntity) {
        return LocationDTO.builder()
                .id(locationEntity.getId())
                .address(locationEntity.getAddress())
                .city(locationEntity.getCity())
                .district(locationEntity.getDistrict())
                .commune(locationEntity.getCommune())
                .userDTO(toUserDTO(locationEntity.getUser()))
                .build();
    }

    public static Set<LocationDTO> toLocationDTOs(final Set<LocationEntity> locationEntities) {
        return locationEntities.stream()
                .map(locationEntity -> toLocationDTO(locationEntity))
                .collect(Collectors.toSet());
    }
}
