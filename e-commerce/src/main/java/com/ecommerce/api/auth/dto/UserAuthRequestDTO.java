package com.ecommerce.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class UserAuthRequestDTO {

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

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 30, message = "Password must be at between 6 to 30 characters")
    private String password;

    private Instant createdAt;

    private String address;

    private String city;

    private String district;

    private String commune;
}
