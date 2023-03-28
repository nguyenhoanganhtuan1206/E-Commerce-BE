package com.ecommerce.error;

import org.springframework.http.HttpStatus;

public class ConflictException extends DomainException {

    public ConflictException(String message, Object... args) {
        super(HttpStatus.CONFLICT, message, args);
    }
}
