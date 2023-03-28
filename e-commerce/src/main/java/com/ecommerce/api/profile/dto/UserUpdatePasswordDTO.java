package com.ecommerce.api.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdatePasswordDTO {

    @NotBlank(message = "Current Password cannot be empty")
    @Size(min = 6, max = 30, message = "Current Password must be at between 6 to 30 characters")
    private String currentPassword;

    @NotBlank(message = "New Password cannot be empty")
    @Size(min = 6, max = 30, message = "New Password must be at between 6 to 30 characters")
    private String newPassword;
}
