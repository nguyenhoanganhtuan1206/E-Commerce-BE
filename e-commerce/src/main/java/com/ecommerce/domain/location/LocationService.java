package com.ecommerce.domain.location;

import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.persistent.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.location.mapper.LocationEntityMapper.toLocationDTO;
import static com.ecommerce.domain.location.mapper.LocationEntityMapper.toLocationEntity;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationDTO save(final LocationDTO locationDTO) {
        return toLocationDTO(locationRepository.save(toLocationEntity(locationDTO)));
    }
}
