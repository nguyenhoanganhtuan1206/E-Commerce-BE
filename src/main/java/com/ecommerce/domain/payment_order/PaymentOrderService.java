package com.ecommerce.domain.payment_order;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart_product_inventory.CartProductInventoryService;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.payment_order.dto.PaymentOrderRequestDTO;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.payment_order.PaymentOrderRepository;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.payment_order.PaymentOrderError.supplyExceedsCurrentQuantity;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    private final CartProductInventoryService cartProductInventoryService;

    private final CommonProductService commonProductService;

    private final InventoryService inventoryService;

    private final CartService cartService;

    private final PaymentMethodService paymentMethodService;

    private final UserService userService;

    private final AuthsProvider authsProvider;

    private static final long SHIPPING_FEE = 20;

//    public void createPaymentOrder(final PaymentOrderRequestDTO paymentOrder) {
//        if (paymentOrder.getCartId() == null) {
//            throw supplyErrorProcesses("You cannot make your cart empty!").get();
//        }
//
//        if (isBlank(paymentOrder.getPaymentMethod())) {
//            throw supplyErrorProcesses("You have to select your payment method!").get();
//        }
//
//        cartProductInventoryService.findByCartId(paymentOrder.getCartId())
//                .forEach(this::verifyWhetherQuantityInStock);
//
//        paymentOrderRepository.save(buildPaymentOrder(paymentOrder, paymentOrder.getCartIds()));
//        updateQuantityProductAfterPaymentSuccessfully(paymentOrder.getCartIds());
//    }
//
//    public List<PaymentOrderDetailDTO> findOrdersBySellerId() {
//        final UUID sellerId = userService.findById(authsProvider.getCurrentUserId()).getSeller().getId();
//        final List<PaymentOrderEntity> paymentOrders = paymentOrderRepository.findBySellerId(sellerId);
//
//        for (final PaymentOrderEntity paymentOrder : paymentOrders) {
//            final PaymentOrderDetailDTO paymentOrderDetailDTO = PaymentOrderDetailDTO.builder()
//                    .id(paymentOrder.getId())
//                    .orderedAt(paymentOrder.getOrderedAt())
//                    .totalPrice(paymentOrder.getTotalPrice())
//                    .deliveryStatus(paymentOrder.getDeliveryStatus())
//                    .email(paymentOrder.getEmailAddress())
//                    .phoneNumber(paymentOrder.getPhoneNumber())
//                    .location(paymentOrder.getLocation())
//                    .address(paymentOrder.getAddress())
//                    .paymentMethod(toPaymentMethodDTO(paymentOrder.getPaymentMethod()))
//                    .build();
//
//            final List<InventoryEntity> inventoryEntities = new ArrayList<>();
//            final List<InventoryEntity> inventoryEntities = new ArrayList<>();
//
//            for (final CartEntity cart : paymentOrder.getCarts()) {
//                paymentOrderDetailDTO.setProductName(cart.getProduct().getName());
//                paymentOrderDetailDTO.setQuantity(cart.getQuantity());
//                paymentOrderDetailDTO.setCategories(cart.getProduct().getCategories().stream()
//                        .map(CategoryEntity::getCategoryName).toList());
//
//                if (cart.getInventory() != null) {
//                    paymentOrderDetailDTO.setInventories();
//                }
//            }
//
//        }
//    }
//
//    private void updateQuantityProductAfterPaymentSuccessfully(final List<UUID> cartIds) {
//        for (final UUID cartId : cartIds) {
//            final CartEntity currentCart = cartService.findById(cartId);
//            if (cartId != null) {
//                final ProductEntity product = commonProductService.findById(cartId);
//                product.setQuantity(product.getQuantity() - currentCart.getQuantity());
//                productRepository.save(product);
//            }
//
//            if (currentCart.getInventory() != null) {
//                final InventoryEntity inventory = inventoryService.findById(currentCart.getInventory().getId());
//                inventory.setQuantity(inventory.getQuantity() - currentCart.getQuantity());
//                inventoryService.save(inventory);
//            }
//        }
//    }

    private void verifyWhetherQuantityInStock(final CartProductInventoryEntity cartProductInventory) {
        if (cartProductInventory.getProductId() != null) {
            final ProductEntity productOrdered = commonProductService.findById(cartProductInventory.getProductId());

            if (cartProductInventory.getQuantity() > productOrdered.getQuantity()) {
                throw supplyExceedsCurrentQuantity(productOrdered.getName(), productOrdered.getQuantity()).get();
            }
        }

        if (cartProductInventory.getInventoryId() != null) {
            final InventoryEntity inventoryOrdered = inventoryService.findById(cartProductInventory.getInventoryId());
            final String preMessage = inventoryOrdered.getProduct().getName() + " with " + inventoryOrdered.getColorValue() + ", " + inventoryOrdered.getSizeValue();

            throw supplyExceedsCurrentQuantity(preMessage, inventoryOrdered.getQuantity()).get();
        }
    }

//    private long calculateTotalPrice(final List<UUID> cartsId) {
//        final long totalPrice = cartsId.stream()
//                .map(cartService::findById)
//                .mapToLong(CartEntity::getTotalPrice)
//                .sum();
//
//        if (totalPrice > 300) {
//            return totalPrice;
//        }
//
//        return totalPrice + SHIPPING_FEE;
//    }
//
//    private PaymentOrderEntity buildPaymentOrder(final PaymentOrderRequestDTO paymentOrderRequest, final List<UUID> currentCartIds) {
//        final PaymentOrderEntity paymentOrderCreate = PaymentOrderEntity.builder()
//                .location(paymentOrderRequest.getLocation())
//                .username(paymentOrderRequest.getUsername())
//                .emailAddress(paymentOrderRequest.getEmailAddress())
//                .phoneNumber(paymentOrderRequest.getPhoneNumber())
//                .address(paymentOrderRequest.getAddress())
//                .totalPrice(calculateTotalPrice(paymentOrderRequest.getCartIds()))
//                .orderedAt(Instant.now())
//                .deliveryStatus(DeliveryStatus.WAITING_PICKUP)
//                .build();
//
//        final List<CartEntity> cartEntities = new ArrayList<>();
//        for (final UUID cartId : currentCartIds) {
//            final CartEntity currentCart = cartService.findById(cartId);
//
//            cartEntities.add(currentCart);
//        }
//
//        if (paymentOrderRequest.getPaymentMethod().equals("COD")) {
//            paymentOrderCreate.setPaymentStatus(PaymentStatus.UNPAID);
//            paymentOrderCreate.setPaymentMethod(paymentMethodService.findByName("COD"));
//        }
//
//        if (paymentOrderRequest.getPaymentMethod().equals("Paypal")) {
//            paymentOrderCreate.setPaymentStatus(PaymentStatus.PAID);
//            paymentOrderCreate.setPaymentMethod(paymentMethodService.findByName("Paypal"));
//        }
//
//        return null;
////        return paymentOrderRepository.save(paymentOrderCreate.withCarts(cartEntities));
//    }
}
