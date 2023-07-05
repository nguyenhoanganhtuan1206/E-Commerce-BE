package com.ecommerce.configuration.dummy_data.product;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.brand.BrandRepository;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.category.CategoryRepository;
import com.ecommerce.persistent.style.ProductStyleEntity;
import com.ecommerce.persistent.style.ProductStyleRepository;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import com.ecommerce.persistent.variant.CategoryVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataProductSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final CategoryVariantRepository categoryVariantRepository;

    private final ProductStyleRepository productStyleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        seedData();
    }

    private void seedData() {
        final List<String> variantsFashions = List.of("Shirt", "Trouser", "Dress", "Shoes", "Bags", "Jean");
        final List<String> brands = List.of("Dior", "Gucci", "Nike", "Adidas", "Coolmate", "Anna");
        final List<String> categories = List.of("Sportswear", "Men", "Women", "Kids", "Accessories", "Sportswear");
        final List<String> styles = List.of("Plain", "Pattern", "Polka Dots", "Plaid");

        seedDataCategories(categories);
        seedDataBrands(brands);
        seedDataVariants(variantsFashions);
        seedDataStyles(styles);
    }

    private void seedDataCategories(final List<String> categories) {
        categories
                .forEach(categoryName -> {
                    if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
                        categoryRepository.save(new CategoryEntity(categoryName));
                    }
                });
    }

    private void seedDataBrands(final List<String> brands) {
        brands
                .forEach(brandName -> {
                    if (brandRepository.findByBrandName(brandName).isEmpty()) {
                        brandRepository.save(new BrandEntity(brandName));
                    }
                });
    }

    private void seedDataVariants(final List<String> variants) {
        variants
                .forEach(variantName -> {
                    if (categoryVariantRepository.findByName(variantName).isEmpty()) {
                        categoryVariantRepository.save(new CategoryVariantEntity(variantName));
                    }
                });
    }

    private void seedDataStyles(final List<String> styles) {
        styles
                .forEach(styleName -> {
                    if (productStyleRepository.findByName(styleName).isEmpty()) {
                        productStyleRepository.save(new ProductStyleEntity(styleName));
                    }
                });
    }
}
