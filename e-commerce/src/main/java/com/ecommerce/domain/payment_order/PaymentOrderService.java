package com.ecommerce.domain.payment_order;

import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.payment_order.dto.PaymentOrderRequestDTO;
import com.ecommerce.domain.payment_status.PaymentStatus;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.payment_order.PaymentOrderEntity;
import com.ecommerce.persistent.payment_order.PaymentOrderRepository;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.payment_order.PaymentOrderError.supplyExceedsCurrentQuantity;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    private final ProductRepository productRepository;

    private final CommonProductService commonProductService;

    private final InventoryService inventoryService;

    private final CartService cartService;

    private final PaymentMethodService paymentMethodService;

    private final long SHIPPING_FEE = 20;

    public PaymentOrderEntity createPaymentOrder(final PaymentOrderRequestDTO paymentOrder) {
        if (paymentOrder.getCartIds().isEmpty()) {
            throw supplyErrorProcesses("You cannot make your cart empty!").get();
        }

        if (isBlank(paymentOrder.getPaymentMethod())) {
            throw supplyErrorProcesses("You have to select your payment method!").get();
        }

        paymentOrder.getCartIds().forEach(this::verifyWhetherQuantityInStock);

        final PaymentOrderEntity paymentOrderCreated = paymentOrderRepository.save(buildPaymentOrder(paymentOrder));

        updateQuantityProductAfterPaymentSuccessfully(paymentOrderCreated, paymentOrderCreated.getCarts());

        return paymentOrderCreated;
    }

    private void updateQuantityProductAfterPaymentSuccessfully(final PaymentOrderEntity paymentOrder, final List<CartEntity> carts) {
        for (final CartEntity cart : carts) {
            if (cart.getProduct() != null) {
                final ProductEntity product = commonProductService.findById(cart.getProduct().getId());
                product.setQuantity(product.getQuantity() - cart.getQuantity());
                productRepository.save(product);
            }

            if (cart.getInventory() != null) {
                final InventoryEntity inventory = inventoryService.findById(cart.getInventory().getId());
                inventory.setQuantity(inventory.getQuantity() - cart.getQuantity());
                inventoryService.save(inventory);
            }

            cartService.save(cart.withPaymentOrder(paymentOrder));
        }
    }

    private void verifyWhetherQuantityInStock(final UUID cartId) {
        final CartEntity cart = cartService.findById(cartId);
        final ProductEntity product = cart.getProduct();
        final InventoryEntity inventory = cart.getInventory();

        if (product != null && cart.getQuantity() > product.getQuantity()) {
            throw supplyExceedsCurrentQuantity(product.getName(), product.getQuantity()).get();
        }

        if (inventory != null && cart.getQuantity() > inventory.getQuantity()) {
            final InventoryEntity inventoryProduct = cart.getInventory();
            final String preMessage = inventoryProduct.getProduct().getName() + " with " + inventoryProduct.getColorValue() + ", " + inventoryProduct.getSizeValue();
            
            throw supplyExceedsCurrentQuantity(preMessage, inventoryProduct.getQuantity()).get();
        }
    }

    private long calculateTotalPrice(final List<UUID> cartsId) {
        final long totalPrice = cartsId.stream()
                .map(cartService::findById)
                .mapToLong(CartEntity::getTotalPrice)
                .sum();

        if (totalPrice > 300) {
            return totalPrice;
        }

        return totalPrice + SHIPPING_FEE;
    }

    private PaymentOrderEntity buildPaymentOrder(final PaymentOrderRequestDTO paymentOrderRequest) {
        final PaymentOrderEntity paymentOrderCreate = PaymentOrderEntity.builder()
                .totalPrice(calculateTotalPrice(paymentOrderRequest.getCartIds()))
                .orderedAt(Instant.now())
                .deliveryStatus(DeliveryStatus.WAITING_PICKUP)
                .carts(buildCarts(paymentOrderRequest.getCartIds()))
                .build();

        if (paymentOrderRequest.getPaymentMethod().equals("COD")) {
            paymentOrderCreate.setPaymentStatus(PaymentStatus.UNPAID);
            paymentOrderCreate.setPaymentMethod(paymentMethodService.findByName("COD"));
        }

        if (paymentOrderRequest.getPaymentMethod().equals("Paypal")) {
            paymentOrderCreate.setPaymentStatus(PaymentStatus.PAID);
            paymentOrderCreate.setPaymentMethod(paymentMethodService.findByName("Paypal"));
        }

        return paymentOrderCreate;
    }

    private List<CartEntity> buildCarts(final List<UUID> cartIds) {
        return cartIds.stream()
                .map(cartService::findById)
                .toList();
    }
}
