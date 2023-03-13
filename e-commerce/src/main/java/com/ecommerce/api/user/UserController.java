package com.ecommerce.api.user;

import com.ecommerce.api.auth.dto.UserResponseDTO;
import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.api.user.dto.UserDTO;
import com.ecommerce.api.user.dto.UserRequestUpdateDTO;
import com.ecommerce.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.error.CommonError.supplyValidationError;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @PutMapping("/profile/{userId}")
    public UserResponseDTO updateInfo(final @PathVariable UUID userId,
                                      final @Valid @RequestBody UserRequestUpdateDTO userRequestDTO,
                                      final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        return userService.updateInfo(userId, userRequestDTO);
    }

    @PostMapping("/profile/{userId}/locations")
    public LocationDTO addLocation(final @PathVariable UUID userId,
                                   final @Valid @RequestBody LocationDTO locationDTO,
                                   final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        return userService.addLocation(userId, locationDTO);
    }
}
