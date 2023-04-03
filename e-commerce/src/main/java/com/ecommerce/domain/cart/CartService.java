package com.ecommerce.domain.cart;

import com.ecommerce.persistent.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.AuthProvider;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final AuthProvider authProvider;
}
