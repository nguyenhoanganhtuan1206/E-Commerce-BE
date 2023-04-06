package com.ecommerce.domain.location;

import com.ecommerce.domain.location.mapper.LocationDTOMapper;
import com.ecommerce.persistent.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.location.mapper.LocationDTOMapper.*;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationDTO save(final LocationDTO locationDTO) {
        return toLocationDTO(locationRepository.save(toLocationEntity(locationDTO)));
    }

    public Optional<LocationDTO> findByDefaultLocationTrue() {
        return locationRepository.findLocationEntitiesByDefaultLocationTrue()
                .map(LocationDTOMapper::toLocationDTO);
    }

    public Set<LocationDTO> findLocationEntitiesByUserId(final UUID userId) {
        return toLocationDTOs(locationRepository.findLocationEntitiesByUserId(userId));
    }
}
