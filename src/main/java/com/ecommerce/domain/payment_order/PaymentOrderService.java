package com.ecommerce.domain.payment_order;

import com.ecommerce.api.order.dto.OrderDeliveryStatusRequestDTO;
import com.ecommerce.api.order.dto.OrderDetailResponseDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.cart.CartService;
import com.ecommerce.domain.cart_product_inventory.CartProductInventoryService;
import com.ecommerce.domain.category.CategoryDTO;
import com.ecommerce.domain.delivery_status.DeliveryStatus;
import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.payment_order.dto.PaymentOrderDetailResponseDTO;
import com.ecommerce.domain.payment_order.dto.PaymentOrderRequestDTO;
import com.ecommerce.domain.payment_status.PaymentStatus;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.domain.seller.SellerService;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.cart_product_inventory.CartProductInventoryEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.location.LocationEntity;
import com.ecommerce.persistent.payment_order.PaymentOrderEntity;
import com.ecommerce.persistent.payment_order.PaymentOrderRepository;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.cart_product_inventory.mapper.CartProductInventoryDTOMapper.toCartProductInventoryDTO;
import static com.ecommerce.domain.category.mapper.CategoryDTOMapper.toCategoryDTOs;
import static com.ecommerce.domain.payment_order.PaymentOrderError.supplyExceedsCurrentQuantity;
import static com.ecommerce.domain.payment_order.PaymentOrderError.supplyPaymentOrderNotFound;
import static com.ecommerce.domain.payment_order.mapper.PaymentOrderDTOMapper.toPaymentOrderDTO;
import static com.ecommerce.domain.product.mapper.ProductDTOMapper.toProductDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static java.time.Instant.now;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    private final CartProductInventoryService cartProductInventoryService;

    private final CommonProductService commonProductService;

    private final InventoryService inventoryService;

    private final CartService cartService;

    private final SellerService sellerService;

    private final UserService userService;

    private final AuthsProvider authProvider;

    private final String ROLE_SELLER = "ROLE_SELLER";

    private static final long SHIPPING_FEE = 20;

    private static final String PAYMENT_WITH_COD = "COD";
    private static final String PAYMENT_WITH_PAYPAL = "Paypal";

    public PaymentOrderEntity findById(final UUID id) {
        return paymentOrderRepository.findById(id)
                .orElseThrow(supplyPaymentOrderNotFound("ID", id));
    }

    public PaymentOrderEntity findByCartId(final UUID cartId) {
        return paymentOrderRepository.findByCartId(cartId)
                .orElseThrow(supplyPaymentOrderNotFound("Cart ID", cartId));
    }

    public List<OrderDetailResponseDTO> findAllOrdersByCurrentUser() {
        return toOrderDetailResponseDTOs(cartProductInventoryService.findByUserIdAndStatusPayment(authProvider.getCurrentUserId(), Boolean.TRUE));
    }

    public List<OrderDetailResponseDTO> findByUserIdAndPaidAndPaymentStatusAndDeliveryStatus(final PaymentStatus paymentStatus, final DeliveryStatus deliveryStatus) {
        return toOrderDetailResponseDTOs(cartProductInventoryService.findByUserIdAndPaidAndPaymentStatusAndDeliveryStatus(authProvider.getCurrentUserId(), paymentStatus, deliveryStatus));
    }

    public List<OrderDetailResponseDTO> findByUserIdAndPaidAndPaymentStatus(final PaymentStatus paymentStatus) {
        return toOrderDetailResponseDTOs(cartProductInventoryService.findByUserIdAndPaymentStatus(authProvider.getCurrentUserId(), paymentStatus));
    }

    public List<OrderDetailResponseDTO> findByPaymentOrderId(final UUID paymentOrderId) {
        return toOrderDetailResponseDTOs(cartProductInventoryService.findByPaymentOrderId(paymentOrderId));
    }

    public List<PaymentOrderDetailResponseDTO> findByCurrentSeller() {
        final SellerEntity currentSeller = sellerService.findByUserId(authProvider.getCurrentUserId());

        return toPaymentOrderDetailResponseDTOs(paymentOrderRepository.findBySellerId(currentSeller.getId()));
    }

    @Transactional
    public void createPaymentOrder(final PaymentOrderRequestDTO paymentOrderRequest) {
        validatePaymentRequest(paymentOrderRequest);

        final CartEntity currentCart = cartService.findById(paymentOrderRequest.getCartId());
        validatePaymentLocation(currentCart.getUser());

        final List<CartProductInventoryEntity> cartProductInventoryEntities = cartProductInventoryService.findByCartId(paymentOrderRequest.getCartId());

        cartProductInventoryEntities.forEach(this::verifyWhetherQuantityInStock);

        paymentOrderRepository.save(buildPaymentOrder(paymentOrderRequest, currentCart));
        updateQuantityProductAfterPaymentSuccessfully(cartProductInventoryEntities);
        cartService.save(currentCart
                .withPayment(Boolean.TRUE));
    }

    public void updateDeliveryStatus(final OrderDeliveryStatusRequestDTO deliveryStatusRequest) {
        final UserEntity currentUser = userService.findById(authProvider.getCurrentUserId());
        final PaymentOrderEntity currentPaymentOrder = findById(deliveryStatusRequest.getOrderPaymentId());

        validateUserUpdateDeliveryStatus(currentUser, deliveryStatusRequest);

        paymentOrderRepository.save(currentPaymentOrder
                .withDeliveryStatus(deliveryStatusRequest.getDeliveryStatus()));
    }

    private boolean hasSellerRole(Set<RoleEntity> currentRoles) {
        return currentRoles.stream()
                .map(RoleEntity::getName)
                .anyMatch(s -> s.equals(ROLE_SELLER));
    }

    private void validateUserUpdateDeliveryStatus(final UserEntity currentUser, final OrderDeliveryStatusRequestDTO deliveryStatusRequest) {
        final DeliveryStatus deliveryStatus = deliveryStatusRequest.getDeliveryStatus();

        if (deliveryStatusRequest.getOrderPaymentId() == null) {
            throw supplyErrorProcesses("You cannot leave ID empty").get();
        }

        if (deliveryStatusRequest.getDeliveryStatus() == null) {
            throw supplyErrorProcesses("You cannot leave Delivery Status empty").get();
        }

        if (!hasSellerRole(currentUser.getRoles())) {
            if (deliveryStatus.equals(DeliveryStatus.WAITING_CONFIRM) || deliveryStatus.equals(DeliveryStatus.WAITING_PICKUP) ||
                    deliveryStatus.equals(DeliveryStatus.SHIPPING) || deliveryStatus.equals(DeliveryStatus.DELIVERED)) {
                throw supplyErrorProcesses("As User you cannot do this action!").get();
            }
        }
    }

    private void validatePaymentLocation(final UserEntity currentUser) {
        if (currentUser.getLocations().isEmpty()) {
            throw supplyErrorProcesses("You have to provide your location!").get();
        }

        final boolean hasDefaultLocation = currentUser.getLocations().stream()
                .anyMatch(LocationEntity::isDefaultLocation);

        if (!hasDefaultLocation) {
            throw supplyErrorProcesses("At least one location must be set as default!").get();
        }
    }

    private void validatePaymentRequest(final PaymentOrderRequestDTO paymentOrderRequest) {
        if (paymentOrderRequest.getCartId() == null) {
            throw supplyErrorProcesses("You cannot make your cart empty!").get();
        }

        if (isBlank(paymentOrderRequest.getPaymentMethodName())) {
            throw supplyErrorProcesses("You have to select your payment method!").get();
        }

        if (!StringUtils.equals(paymentOrderRequest.getPaymentMethodName(), PAYMENT_WITH_COD) &&
                !StringUtils.equals(paymentOrderRequest.getPaymentMethodName(), PAYMENT_WITH_COD)) {
            throw supplyErrorProcesses("Seem this payment method you selected it not existed. Please try again!").get();
        }
    }

    private void verifyWhetherQuantityInStock(final CartProductInventoryEntity cartProductInventory) {
        if (cartProductInventory.getProductId() != null) {
            final ProductEntity productSelected = commonProductService.findById(cartProductInventory.getProductId());

            if (cartProductInventory.getQuantity() > productSelected.getQuantity()) {
                throw supplyExceedsCurrentQuantity(productSelected.getName(), productSelected.getQuantity()).get();
            }
        }

        if (cartProductInventory.getInventoryId() != null) {
            final InventoryEntity inventorySelected = inventoryService.findById(cartProductInventory.getInventoryId());
            final String preMessage = inventorySelected.getProduct().getName() + " with " + inventorySelected.getColorValue() + ", " + inventorySelected.getSizeValue();

            if (cartProductInventory.getQuantity() > inventorySelected.getQuantity()) {
                throw supplyExceedsCurrentQuantity(preMessage, inventorySelected.getQuantity()).get();
            }
        }
    }

    private void updateQuantityProductAfterPaymentSuccessfully(final List<CartProductInventoryEntity> cartProductInventoryEntities) {
        for (final CartProductInventoryEntity cartProductInventory : cartProductInventoryEntities) {
            if (cartProductInventory.getProductId() != null) {
                final ProductEntity product = commonProductService.findById(cartProductInventory.getProductId());
                product.setQuantity(product.getQuantity() - cartProductInventory.getQuantity());
                commonProductService.save(product);
            }

            if (cartProductInventory.getInventoryId() != null) {
                final InventoryEntity inventory = inventoryService.findById(cartProductInventory.getInventoryId());
                inventory.setQuantity(inventory.getQuantity() - cartProductInventory.getQuantity());
                inventoryService.save(inventory);
            }
        }
    }

    private PaymentOrderEntity buildPaymentOrder(final PaymentOrderRequestDTO paymentOrderRequest,
                                                 final CartEntity currentCart) {
        final PaymentOrderEntity paymentOrderCreate = PaymentOrderEntity.builder()
                .paymentMethodName(paymentOrderRequest.getPaymentMethodName())
                .cartId(paymentOrderRequest.getCartId())
                .orderedAt(now())
                .deliveryStatus(DeliveryStatus.WAITING_CONFIRM)
                .build();

        if (paymentOrderRequest.getPaymentMethodName().equals(PAYMENT_WITH_COD)) {
            paymentOrderCreate.setPaymentStatus(PaymentStatus.UNPAID);
            paymentOrderCreate.setPaymentMethodName(PAYMENT_WITH_COD);
        }

        if (paymentOrderRequest.getPaymentMethodName().equals(PAYMENT_WITH_PAYPAL)) {
            paymentOrderCreate.setPaymentStatus(PaymentStatus.PAID);
            paymentOrderCreate.setPaymentMethodName(PAYMENT_WITH_PAYPAL);
        }

        return paymentOrderRepository.save(paymentOrderCreate
                .withTotalPrice(calculateTotalPrice(currentCart)));
    }

    private long calculateTotalPrice(final CartEntity currentCart) {
        if (currentCart.getTotalPrice() > 300) {
            return currentCart.getTotalPrice();
        }

        return currentCart.getTotalPrice() + SHIPPING_FEE;
    }

    private PaymentOrderDetailResponseDTO toPaymentOrderDetailResponseDTO(final PaymentOrderEntity paymentOrder) {
        final CartEntity currentCart = cartService.findById(paymentOrder.getCartId());
        final List<CartProductInventoryEntity> cartProductInventories = cartProductInventoryService.findBySellerId(currentCart.getSellerId());

        final PaymentOrderDetailResponseDTO paymentOrderDetailResponseDTO = PaymentOrderDetailResponseDTO.builder()
                .id(paymentOrder.getId())
                .buyer(toUserDTO(currentCart.getUser()))
                .paymentMethodName(paymentOrder.getPaymentMethodName())
                .orderedAt(paymentOrder.getOrderedAt())
                .totalPrice(paymentOrder.getTotalPrice())
                .paymentStatus(paymentOrder.getPaymentStatus())
                .deliveryStatus(paymentOrder.getDeliveryStatus())
                .build();


        for (final CartProductInventoryEntity cartProductInventory : cartProductInventories) {
            if (cartProductInventory.getProductId() != null) {
                final ProductEntity productSelected = commonProductService.findById(cartProductInventory.getProductId());

                paymentOrderDetailResponseDTO.setProduct(toProductDTO(productSelected));
                paymentOrderDetailResponseDTO.setCategories(convertCategoryDTOsToStrings(toCategoryDTOs(productSelected.getCategories())));
            }

            if (cartProductInventory.getInventoryId() != null) {
                final InventoryEntity inventorySelected = inventoryService.findById(cartProductInventory.getInventoryId());

                paymentOrderDetailResponseDTO.setProduct(toProductDTO(inventorySelected.getProduct()));
                paymentOrderDetailResponseDTO.setCategories(convertCategoryDTOsToStrings(toCategoryDTOs(inventorySelected.getProduct().getCategories())));
            }
        }

        return paymentOrderDetailResponseDTO;
    }

    private List<PaymentOrderDetailResponseDTO> toPaymentOrderDetailResponseDTOs(final List<PaymentOrderEntity> paymentOrderEntities) {
        return paymentOrderEntities.stream()
                .map(this::toPaymentOrderDetailResponseDTO)
                .toList();
    }

    private List<String> convertCategoryDTOsToStrings(final List<CategoryDTO> categoryDTOS) {
        return categoryDTOS.stream()
                .map(CategoryDTO::toString)
                .toList();
    }

    private OrderDetailResponseDTO toOrderDetailResponseDTO(final CartProductInventoryEntity cartProductInventory) {
        final CartEntity currentCart = cartService.findById(cartProductInventory.getCartId());
        final PaymentOrderEntity paymentOrder = findByCartId(currentCart.getId());

        final OrderDetailResponseDTO cartDetailResponseDTO = OrderDetailResponseDTO.builder()
                .id(cartProductInventory.getCartId())
                .totalPrice(currentCart.getTotalPrice())
                .user(toUserDTO(currentCart.getUser()))
                .cartProductInventory(toCartProductInventoryDTO(cartProductInventory))
                .paymentOrder(toPaymentOrderDTO(paymentOrder))
                .build();

        if (cartProductInventory.getInventoryId() != null) {
            final InventoryEntity currentInventory = inventoryService.findById(cartProductInventory.getInventoryId());

            cartDetailResponseDTO.setProduct(toProductDTO(currentInventory.getProduct()));
            cartDetailResponseDTO.setSeller(toSellerDTO(currentInventory.getProduct().getSeller()));
        }

        if (cartProductInventory.getProductId() != null) {
            final ProductEntity currentProduct = commonProductService.findById(cartProductInventory.getProductId());

            cartDetailResponseDTO.setProduct(toProductDTO(currentProduct));
            cartDetailResponseDTO.setSeller(toSellerDTO(currentProduct.getSeller()));
        }

        return cartDetailResponseDTO;
    }

    private List<OrderDetailResponseDTO> toOrderDetailResponseDTOs(final List<CartProductInventoryEntity> carts) {
        return carts.stream()
                .map(this::toOrderDetailResponseDTO)
                .toList();
    }
}
