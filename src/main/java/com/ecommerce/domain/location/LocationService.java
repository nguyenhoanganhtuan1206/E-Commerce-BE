package com.ecommerce.domain.location;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.location.LocationEntity;
import com.ecommerce.persistent.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.location.LocationError.supplyLocationNotFound;
import static com.ecommerce.domain.location.LocationValidation.verifyIfLocationExisted;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final UserService userService;

    private final AuthsProvider authsProvider;

    public List<LocationEntity> findLocationsByUserIdAndSorted(final UUID userId) {
        return locationRepository.findLocationsByUserIdAndSorted(userId);
    }

    public void delete(final UUID locationId) {
        locationRepository.delete(findById(locationId));
    }

    public LocationEntity findById(final UUID locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(supplyLocationNotFound(locationId));
    }

    public LocationEntity addLocation(final LocationRequestDTO locationRequestDTO) {
        final List<LocationEntity> locations = findLocationsByUserId(authsProvider.getCurrentUserId());

        verifyIfLocationExisted(locations, locationRequestDTO);

        if (locationRequestDTO.isDefaultLocation()) {
            changeDefaultLocationIfAvailable();
        }

        final LocationEntity location = LocationEntity.builder()
                .address(locationRequestDTO.getAddress())
                .province(locationRequestDTO.getProvince())
                .district(locationRequestDTO.getDistrict())
                .commune(locationRequestDTO.getCommune())
                .defaultLocation(locationRequestDTO.isDefaultLocation())
                .createdAt(Instant.now())
                .user(userService.findById(authsProvider.getCurrentUserId()))
                .build();

        return locationRepository.save(location);
    }

    public LocationEntity setDefaultLocation(final UUID locationId) {
        final LocationEntity locationFound = findById(locationId);
        changeDefaultLocationIfAvailable();
        locationFound.setDefaultLocation(true);

        return locationRepository.save(locationFound);
    }

    public LocationEntity updateLocation(final UUID locationId, final LocationRequestDTO locationRequestDTO) {
        final LocationEntity locationFound = findById(locationId);
        final List<LocationEntity> locationsExisted = findLocationsByUserId(authsProvider.getCurrentUserId());

        if (locationRequestDTO.isDefaultLocation()) {
            changeDefaultLocationIfAvailable();
        }

        verifyIfLocationExisted(locationsExisted, locationRequestDTO);

        locationFound.setAddress(locationRequestDTO.getAddress());
        locationFound.setProvince(locationRequestDTO.getProvince());
        locationFound.setCommune(locationRequestDTO.getCommune());
        locationFound.setDistrict(locationRequestDTO.getDistrict());
        locationFound.setDefaultLocation(locationRequestDTO.isDefaultLocation());
        locationFound.setUpdatedAt(Instant.now());

        return locationRepository.save(locationFound);
    }

    public Optional<LocationEntity> findByDefaultLocationTrue() {
        return locationRepository.findByDefaultLocationTrue();
    }

    public List<LocationEntity> findLocationsByUserId(final UUID userId) {
        return locationRepository.findLocationEntitiesByUserId(userId);
    }

    private void changeDefaultLocationIfAvailable() {
        findByDefaultLocationTrue()
                .ifPresent(location -> {
                    location.setDefaultLocation(false);
                    locationRepository.save(location);
                });
    }
}
