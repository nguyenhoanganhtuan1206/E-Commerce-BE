package com.ecommerce.domain.user;

import lombok.experimental.UtilityClass;

import static com.ecommerce.error.CommonError.supplyValidationError;

@UtilityClass
public class UserValidation {

    private static final String PATTERN_USERNAME = "^[a-zA-Z ]+$";

    public static void validateUsername(final String username) {
        if (!username.matches(PATTERN_USERNAME)) {
            throw supplyValidationError("Username is invalid").get();
        }
    }
}
