package com.ecommerce.domain.product;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.ecommerce.error.CommonError.supplyValidationError;

@UtilityClass
public class ProductVariation {

    public static void validatePriceProduct(final double price) {
        if (price < 5 || price > 1000000) {
            throw supplyValidationError("Price must be between 5 and 1000000.").get();
        }
    }

    public static void validateQuantityProduct(final long quantity) {
        if (quantity > 100000) {
            throw supplyValidationError("Price must be less than 10000 unit.").get();
        }
    }

    public static void validatePaymentMethodNotEmpty(final List<String> paymentMethods) {
        if (paymentMethods.isEmpty()) {
            throw supplyValidationError("Shipping options required. Please choose at least one.").get();
        }
    }

    public static void validateCategoriesNotEmpty(final List<String> categories) {
        if (categories.isEmpty()) {
            throw supplyValidationError("Categories required. Please choose at least one.").get();
        }
    }

    public static void validateProductStylesNotEmpty(final List<String> productStyles) {
        if (productStyles.isEmpty()) {
            throw supplyValidationError("Product Styles required. Please choose at least one.").get();
        }
    }
}
