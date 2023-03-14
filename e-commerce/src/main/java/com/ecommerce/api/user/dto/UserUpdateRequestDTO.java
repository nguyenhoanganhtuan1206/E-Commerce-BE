package com.ecommerce.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDTO {

    /**
     * @ This request DTO without password
     */

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 6, max = 30, message = "Username must be at between 6 to 30 characters")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is invalid")
    @Size(min = 9, max = 30, message = "Email must be at between 9 to 30 characters")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 9, max = 11, message = "Phone number is invalid")
    private String phoneNumber;

    /**
     * @ Password is optional
     * */
    private String password;
}
