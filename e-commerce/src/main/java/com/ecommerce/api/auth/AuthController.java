package com.ecommerce.api.auth;

import com.ecommerce.api.auth.dto.UserRequestDTO;
import com.ecommerce.api.auth.dto.UserResponseDTO;
import com.ecommerce.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ecommerce.error.CommonError.supplyValidationError;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public UserResponseDTO signUp(final @Valid @RequestBody UserRequestDTO userRequestDTO,
                                  final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        return userService.signUp(userRequestDTO);
    }
}
