package com.ecommerce.api.user;

import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.api.user.dto.UserUpdateRequestDTO;
import com.ecommerce.api.user.dto.UserUpdateResponseDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/profile/{userId}")
    public UserDTO findById(final @PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/profile/{userId}")
    public UserUpdateResponseDTO updateInfo(final @PathVariable UUID userId,
                                            final @Valid @RequestBody UserUpdateRequestDTO userRequestDTO,
                                            final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return userService.updateInfo(userId, userRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/profile/{userId}/locations")
    public LocationDTO addLocation(final @PathVariable UUID userId,
                                   final @Valid @RequestBody LocationDTO locationDTO,
                                   final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return userService.addLocation(userId, locationDTO);
    }
}
