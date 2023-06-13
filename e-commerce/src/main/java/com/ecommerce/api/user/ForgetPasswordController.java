package com.ecommerce.api.user;

import com.ecommerce.api.user.dto.UserRequestConfirmEmailDTO;
import com.ecommerce.api.user.dto.UserRequestResetPasswordDTO;
import com.ecommerce.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class ForgetPasswordController {

    private final UserService userService;

    @PostMapping("forget-password")
    public void handleForgetPassword(final @Valid @RequestBody UserRequestConfirmEmailDTO userRequestDTO, final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        userService.sendEmailForgetPassword(userRequestDTO.getEmail());
    }

    @GetMapping("check-expiration-code")
    public void verifyCodeResetPassword(final @RequestParam(value = "code") String code) {
        userService.verifyCodeResetPassword(code);
    }

    @PutMapping("reset-password")
    public void handleResetPassword(final @Valid @RequestBody UserRequestResetPasswordDTO userRequestDTO,
                                    final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        userService.resetPassword(userRequestDTO);
    }
}
