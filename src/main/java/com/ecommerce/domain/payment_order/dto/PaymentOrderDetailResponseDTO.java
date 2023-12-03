package com.ecommerce.domain.payment_order.dto;

import com.ecommerce.domain.cart_product_inventory.dto.CartProductInventoryDTO;
import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_status.PaymentStatus;
import com.ecommerce.domain.product.dto.ProductDTO;
import com.ecommerce.domain.user.UserDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderDetailResponseDTO {

    private UUID id;

    private UserDTO buyer;

    private ProductDTO product;

    private List<String> categories;

    private long totalPrice;

    private Instant orderedAt;

    private String paymentMethodName;

    private PaymentStatus paymentStatus;

    private DeliveryStatus deliveryStatus;

    private CartProductInventoryDTO cartProductInventoryDTO;
}
