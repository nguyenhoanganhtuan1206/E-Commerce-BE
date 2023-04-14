package com.ecommerce.domain.location;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.location.mapper.LocationDTOMapper;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.location.LocationError.supplyLocationNotFound;
import static com.ecommerce.domain.location.LocationValidation.verifyIfLocationExisted;
import static com.ecommerce.domain.location.mapper.LocationDTOMapper.*;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final UserService userService;

    private final AuthsProvider authsProvider;

    public LocationDTO save(final LocationDTO locationDTO) {
        locationDTO.setCreatedAt(Instant.now());

        return toLocationDTO(locationRepository.save(toLocationEntity(locationDTO)));
    }

    public List<LocationDTO> findLocationsByUserIdAndSorted() {
        return toLocationDTOs(locationRepository.findLocationsByUserIdAndSorted(authsProvider.getCurrentUserId()));
    }

    public void delete(final UUID locationId) {
        final LocationDTO locationDTO = findById(locationId);

        locationRepository.delete(toLocationEntity(locationDTO));
    }

    public LocationDTO findById(final UUID locationId) {
        return toLocationDTO(locationRepository.findById(locationId)
                .orElseThrow(supplyLocationNotFound(locationId)));
    }

    public LocationDTO addLocation(final LocationRequestDTO locationRequestDTO) {
        final LocationDTO location = toLocationDTO(locationRequestDTO);
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());
        final List<LocationDTO> locationDTOS = findLocationsByUserId(authsProvider.getCurrentUserId());

        verifyIfLocationExisted(locationDTOS, locationRequestDTO);

        if (locationRequestDTO.isDefaultLocation()) {
            changeDefaultLocationIfAvailable();
        }

        location.setCreatedAt(Instant.now());
        location.setUser(userDTO);
        location.setDefaultLocation(locationRequestDTO.isDefaultLocation());

        userService.save(userDTO);
        return save(location);
    }

    public LocationDTO setDefaultLocation(final UUID locationId) {
        final LocationDTO locationDTO = findById(locationId);
        changeDefaultLocationIfAvailable();
        locationDTO.setDefaultLocation(true);

        return toLocationDTO(locationRepository.save(toLocationEntity(locationDTO)));
    }

    public LocationDTO updateLocation(final UUID locationId, final LocationRequestDTO locationRequestDTO) {
        final LocationDTO locationDTO = findById(locationId);
        final List<LocationDTO> locationDTOS = findLocationsByUserId(authsProvider.getCurrentUserId());

        if (locationRequestDTO.isDefaultLocation()) {
            changeDefaultLocationIfAvailable();
        }

        verifyIfLocationExisted(locationDTOS, locationRequestDTO);

        locationDTO.setAddress(locationRequestDTO.getAddress());
        locationDTO.setProvince(locationRequestDTO.getProvince());
        locationDTO.setCommune(locationRequestDTO.getCommune());
        locationDTO.setDistrict(locationRequestDTO.getDistrict());
        locationDTO.setDefaultLocation(locationRequestDTO.isDefaultLocation());
        locationDTO.setUpdatedAt(Instant.now());

        return toLocationDTO(locationRepository.save(toLocationEntity(locationDTO)));
    }

    public Optional<LocationDTO> findByDefaultLocationTrue() {
        return locationRepository.findByDefaultLocationTrue()
                .map(LocationDTOMapper::toLocationDTO);
    }

    public List<LocationDTO> findLocationsByUserId(final UUID userId) {
        return toLocationDTOs(locationRepository.findLocationEntitiesByUserId(userId));
    }

    private void changeDefaultLocationIfAvailable() {
        findByDefaultLocationTrue()
                .ifPresent(location -> {
                    location.setDefaultLocation(false);
                    save(location);
                });
    }
}
