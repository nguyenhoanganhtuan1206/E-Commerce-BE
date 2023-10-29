package com.ecommerce.api.profile;

import com.ecommerce.api.profile.dto.UserUpdatePasswordDTO;
import com.ecommerce.api.profile.dto.UserUpdateRequestDTO;
import com.ecommerce.api.profile.dto.UserUpdateResponseDTO;
import com.ecommerce.api.user.dto.UserResponseDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER', 'ROLE_ADMIN')")
public class ProfileController {

    private final UserService userService;

    private final AuthsProvider authsProvider;

    @GetMapping
    public UserResponseDTO getProfile() {
        return userService.findProfileById(authsProvider.getCurrentUserId());
    }

    @PutMapping
    public UserUpdateResponseDTO update(final @Valid @RequestBody UserUpdateRequestDTO userRequestDTO,
                                        final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return userService.update(userRequestDTO);
    }

    @PutMapping("/update-password")
    public UserUpdateResponseDTO update(final @Valid @RequestBody UserUpdatePasswordDTO userRequestDTO,
                                        final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return userService.updatePassword(userRequestDTO);
    }
}
