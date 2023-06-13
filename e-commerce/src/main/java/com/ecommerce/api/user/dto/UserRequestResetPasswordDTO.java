package com.ecommerce.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestResetPasswordDTO {

    @NotBlank(message = "New Password cannot be empty")
    @Size(min = 6, max = 30, message = "New Password must be at between 6 to 30 characters")
    private String newPassword;

    @NotBlank(message = "Code reset password cannot be empty")
    private String code;
}
