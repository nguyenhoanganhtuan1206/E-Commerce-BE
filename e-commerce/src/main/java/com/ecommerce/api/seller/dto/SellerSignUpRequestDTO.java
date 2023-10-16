package com.ecommerce.api.seller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SellerSignUpRequestDTO {

    @NotBlank(message = "Seller Name cannot be empty")
    @Size(min = 6, max = 50, message = "Seller name must be at between 6 to 50 characters")
    private String sellerName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is invalid")
    @Size(min = 9, message = "Email is invalid")
    private String emailSeller;

    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 9, max = 11, message = "Phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 3, message = "Address is invalid")
    private String address;

    @NotBlank(message = "Province cannot be empty")
    private String province;

    @NotBlank(message = "District cannot be empty")
    private String district;

    @NotBlank(message = "Commune cannot be empty")
    private String commune;

    private Set<String> namePaymentMethods;
}
