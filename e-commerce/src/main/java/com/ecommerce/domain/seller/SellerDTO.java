package com.ecommerce.domain.seller;

import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SellerDTO {

    private UUID id;

    private String sellerName;

    private String emailSeller;

    private String phoneNumber;

    private float sellerRating;

    private boolean sellerStatus;

    private String confirmationToken;

    private String address;

    private String city;

    private String district;

    private String commune;

    private UserDTO user;

    private Set<ProductDTO> products;
}
