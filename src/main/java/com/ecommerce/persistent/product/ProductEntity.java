package com.ecommerce.persistent.product;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.cart.CartEntity;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.inventory.InventoryEntity;
import com.ecommerce.persistent.payment_method.PaymentMethodEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.status.Status;
import com.ecommerce.persistent.style.ProductStyleEntity;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private long price;

    private long quantity;

    @Enumerated(EnumType.STRING)
    private Status productApproval;

    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    private Instant updatedAt;

    @Column(name = "amount_sold_out")
    private Long amountSoldOut;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerEntity seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<InventoryEntity> inventories;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories; // Men, Women

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_variant_id", nullable = false)
    private CategoryVariantEntity categoryVariant; // Bags, ...

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<CartEntity> carts;

    @ManyToMany
    @JoinTable(name = "product_payment_method",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private List<PaymentMethodEntity> paymentMethods;

    @ManyToMany
    @JoinTable(
            name = "product_product_style",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_style_id")
    )
    private List<ProductStyleEntity> productStyles;
}
