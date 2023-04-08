package com.ecommerce.domain.seller;

import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.persistent.status.Status;
import lombok.*;

import java.util.Set;
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

    private String city;

    private String district;

    private String commune;

    private Status sellerApproval;

    private Set<PaymentMethodDTO> paymentMethods;

    private UserDTO user;

    private Set<ProductDTO> products;
}
