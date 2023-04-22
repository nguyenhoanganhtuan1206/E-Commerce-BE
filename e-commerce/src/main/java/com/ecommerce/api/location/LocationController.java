package com.ecommerce.api.location;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.location.dto.LocationResponseDTO;
import com.ecommerce.domain.location.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.location.mapper.LocationDTOMapper.toLocationResponseDTO;
import static com.ecommerce.domain.location.mapper.LocationDTOMapper.toLocationResponseDTOs;
import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER', 'ROLE_ADMIN')")
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/user/{userId}")
    public List<LocationResponseDTO> findLocationsByUserId(@PathVariable final UUID userId) {
        return toLocationResponseDTOs(locationService.findLocationsByUserIdAndSorted(userId));
    }

    @GetMapping("{locationId}")
    public LocationResponseDTO findLocationById(final @PathVariable UUID locationId) {
        return toLocationResponseDTO(locationService.findById(locationId));
    }

    @PostMapping
    public LocationResponseDTO addLocation(final @Valid @RequestBody LocationRequestDTO locationDTO, final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return toLocationResponseDTO(locationService.addLocation(locationDTO));
    }

    @PutMapping("/{locationId}/default")
    public LocationResponseDTO setDefaultLocation(final @PathVariable UUID locationId) {
        return toLocationResponseDTO(locationService.setDefaultLocation(locationId));
    }

    @PutMapping("{locationId}")
    public LocationResponseDTO updateLocation(final @PathVariable UUID locationId,
                                              final @Valid @RequestBody LocationRequestDTO locationRequestDTO,
                                              final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return toLocationResponseDTO(locationService.updateLocation(locationId, locationRequestDTO));
    }

    @DeleteMapping("{locationId}")
    public void deleteLocation(final @PathVariable UUID locationId) {
        locationService.delete(locationId);
    }
}
