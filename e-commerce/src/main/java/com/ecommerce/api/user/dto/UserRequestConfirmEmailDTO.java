package com.ecommerce.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestConfirmEmailDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is invalid")
    @Size(min = 9, message = "Email must be at least 9 characters")
    private String email;
}
