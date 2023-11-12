package com.ecommerce.api.orders;

import com.ecommerce.domain.orders.OrderService;
import com.ecommerce.domain.orders.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping
    public OrderResponseDTO findBySellerId() {
        return null;
    }
}
