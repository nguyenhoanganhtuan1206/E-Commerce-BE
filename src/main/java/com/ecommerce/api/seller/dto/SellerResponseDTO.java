package com.ecommerce.api.seller.dto;

import com.ecommerce.persistent.status.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
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

    private String province;

    private String district;

    private String commune;

    private Instant createdAt;

    private Instant updatedAt;

    private Status sellerApproval;

    private UUID userId;
}
