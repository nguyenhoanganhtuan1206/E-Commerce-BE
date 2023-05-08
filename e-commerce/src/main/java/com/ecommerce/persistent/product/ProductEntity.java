package com.ecommerce.persistent.product;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.status.Status;
import com.ecommerce.persistent.style.ProductStyleEntity;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private long price;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private Status productApproval;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerEntity seller;

    @OneToMany(mappedBy = "product")
    private List<InventoryEntity> inventories;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories; // Men, Women

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private CategoryVariantEntity categoryVariant; // Bags, ...

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<CartEntity> carts;

    @ManyToMany
    @JoinTable(name = "product_payment_method",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private Set<PaymentMethodEntity> paymentMethods;

    @ManyToMany(mappedBy = "products")
    private List<ProductStyleEntity> productStyles;
}
