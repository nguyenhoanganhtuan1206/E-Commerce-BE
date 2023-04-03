package com.ecommerce.api.seller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerCreateRequestDTO {

    @NotBlank(message = "Seller Name cannot be empty")
    @Size(min = 6, max = 50, message = "Seller name must be at between 6 to 50 characters")
    private String sellerName;

    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 9, max = 11, message = "Phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 3, message = "Address is invalid")
    private String address;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "District cannot be empty")
    private String district;

    @NotBlank(message = "Commune cannot be empty")
    private String commune;
}
