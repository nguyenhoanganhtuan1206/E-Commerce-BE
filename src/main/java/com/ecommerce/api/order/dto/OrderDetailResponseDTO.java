package com.ecommerce.api.order.dto;

import com.ecommerce.domain.cart_product_inventory.dto.CartProductInventoryDTO;
import com.ecommerce.domain.payment_order.dto.PaymentOrderDTO;
import com.ecommerce.domain.product.dto.ProductDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.user.UserDTO;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderDetailResponseDTO {

    private UUID id;

    private double totalPrice;

    private SellerDTO seller;

    private ProductDTO product;

    private UserDTO user;

    private CartProductInventoryDTO cartProductInventory;

    private PaymentOrderDTO paymentOrder;
}
