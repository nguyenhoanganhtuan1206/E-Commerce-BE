package com.ecommerce.api.order;

import com.ecommerce.api.order.dto.OrderDetailResponseDTO;
import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.payment_order.PaymentOrderService;
import com.ecommerce.domain.payment_status.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentOrderService paymentOrderService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping
    public List<OrderDetailResponseDTO> findAllOrders() {
        return paymentOrderService.findAllOrdersByCurrentUser();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("completed")
    public List<OrderDetailResponseDTO> findByUserIdAndPaidStatus() {
        return paymentOrderService.findByUserIdAndPaidAndPaymentStatusAndDeliveryStatus(PaymentStatus.PAID, DeliveryStatus.DELIVERED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("waiting-for-payment")
    public List<OrderDetailResponseDTO> findByUserIdAndPaidAndUnpaidStatus() {
        return paymentOrderService.findByUserIdAndPaidAndPaymentStatus(PaymentStatus.UNPAID);
    }
}
