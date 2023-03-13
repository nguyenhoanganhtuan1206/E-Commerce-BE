package com.ecommerce.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {
    public NotFoundException(String message, Object... args) {
        super(HttpStatus.NOT_FOUND, message, args);
    }
}
