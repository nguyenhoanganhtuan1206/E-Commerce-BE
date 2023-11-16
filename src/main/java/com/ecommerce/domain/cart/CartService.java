package com.ecommerce.domain.cart;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.ecommerce.domain.cart.CartError.supplyCartNotFound;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public CartEntity findById(final UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(supplyCartNotFound(cartId, "id"));
    }

    public CartEntity save(final CartEntity cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public void deleteById(final UUID cartId) {
        final CartEntity cartEntity = findById(cartId);

        cartRepository.delete(cartEntity);
    }
}
