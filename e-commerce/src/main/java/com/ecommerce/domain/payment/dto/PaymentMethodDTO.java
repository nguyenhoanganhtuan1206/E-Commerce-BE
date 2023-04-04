package com.ecommerce.domain.payment.dto;

import com.ecommerce.domain.seller.SellerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDTO {

    private UUID id;

    private String name;

    private Set<SellerDTO> sellers;
}
