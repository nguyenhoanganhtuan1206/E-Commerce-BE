package com.ecommerce.domain.seller;

import com.ecommerce.persistent.status.Status;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {

    private UUID id;

    private String sellerName;

    private String emailSeller;

    private String phoneNumber;

    private float sellerRating;

    private String address;

    private String province;

    private String district;

    private String commune;

    private Status sellerApproval;

    private Instant createdAt;

    private Instant updatedAt;
}
