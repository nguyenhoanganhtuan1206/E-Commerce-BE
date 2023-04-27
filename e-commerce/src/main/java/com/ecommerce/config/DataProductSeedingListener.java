package com.ecommerce.config;

import com.ecommerce.persistent.brand.BrandEntity;
import com.ecommerce.persistent.brand.BrandRepository;
import com.ecommerce.persistent.category.CategoryEntity;
import com.ecommerce.persistent.category.CategoryRepository;
import com.ecommerce.persistent.variant.CategoryVariantEntity;
import com.ecommerce.persistent.variant.CategoryVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataProductSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final CategoryVariantRepository categoryVariantRepository;

    private void seedDataCommonCategoriesAndBrands(final String categoryName, final Set<String> variantsSet, final Set<String> brandsSet) {
        if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
            final CategoryEntity category = new CategoryEntity(categoryName);
            final Set<CategoryVariantEntity> variants = new HashSet<>();
            final Set<BrandEntity> brands = new HashSet<>();

            for (Object variantName : variantsSet) {
                if (categoryVariantRepository.findByVariantName(variantName.toString()).isEmpty()) {
                    final CategoryVariantEntity categoryVariant = CategoryVariantEntity.builder()
                            .variantName(variantName.toString())
                            .category(category)
                            .build();
                    variants.add(categoryVariant);
                }
            }
            category.setCategoryVariants(variants);

            for (Object brandName : brandsSet) {
                if (brandRepository.findByBrandName(brandName.toString()).isEmpty()) {
                    final BrandEntity brand = BrandEntity.builder()
                            .brandName(brandName.toString())
                            .category(category)
                            .build();
                    brands.add(brand);
                }
            }
            category.setBrands(brands);
            categoryRepository.save(category);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        seedCategoriesAndBrands();
    }

    private void seedCategoriesAndBrands() {
        final Set<String> variantsTechnology = Set.of("Phone", "Laptop", "Screen", "Headphone");
        final Set<String> variantsFashions = Set.of("Shirt", "Trouser", "Dress", "Shoes");
        final Set<String> fashions = Set.of("Dior", "Gucci", "Nike", "Adidas");
        final Set<String> technologies = Set.of("Lenovo", "Apple", "Samsung", "Logitech");

        seedDataCommonCategoriesAndBrands("Fashion", variantsFashions, fashions);
        seedDataCommonCategoriesAndBrands("Technology", variantsTechnology, technologies);
    }
}
