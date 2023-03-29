package com.ecommerce.api.user;

import com.ecommerce.api.user.dto.UserRequestConfirmEmailDTO;
import com.ecommerce.api.user.dto.UserRequestResetPasswordDTO;
import com.ecommerce.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.error.CommonError.supplyValidationError;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class ForgetPasswordController {

    private final UserService userService;

    @PostMapping("/forget-password")
    public void handleForgetPassword(final @Valid @RequestBody UserRequestConfirmEmailDTO userRequestDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        userService.handleForgetPassword(userRequestDTO.getEmail());
    }

    @PutMapping("/reset-password")
    public void handleResetPassword(final @Valid @RequestBody UserRequestResetPasswordDTO userRequestDTO,
                                    final @RequestParam String token,
                                    final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        userService.handleResetPassword(userRequestDTO, token);
    }
}
