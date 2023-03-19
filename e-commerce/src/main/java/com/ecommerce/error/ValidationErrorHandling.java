package com.ecommerce.error;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static com.ecommerce.error.CommonError.supplyValidationError;

@UtilityClass
public class ValidationErrorHandling {

    public static void handleValidationError(final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                throw supplyValidationError(fieldError.getDefaultMessage()).get();
            }
        }
    }
}
