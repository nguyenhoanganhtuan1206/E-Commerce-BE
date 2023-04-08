package com.ecommerce.api.seller.dto;

import com.ecommerce.persistent.status.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SellerResponseDTO {

    private UUID id;

    private String sellerName;

    private String emailSeller;

    private String phoneNumber;

    private String address;

    private String city;

    private String district;

    private String commune;

    private Status sellerApproval;
}
