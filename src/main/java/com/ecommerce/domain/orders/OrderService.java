package com.ecommerce.domain.orders;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.orders.dto.OrderResponseDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.payment_order.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    private final UserService userService;

    private final AuthsProvider authsProvider;

    public OrderResponseDTO findBySellerId() {
        final UUID sellerId = userService.findById(authsProvider.getCurrentUserId()).getSeller().getId();

        return null;
    }
}
