package com.ecommerce.api.seller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerResponseSignUpDTO {

    private String sellerName;

    private String emailSeller;

    private String phoneNumber;

    private String address;

    private String city;

    private String district;

    private String commune;
}
