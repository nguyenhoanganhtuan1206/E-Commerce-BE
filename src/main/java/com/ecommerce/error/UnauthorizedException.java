package com.ecommerce.error;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException(String message, Object... args) {
        super(HttpStatus.UNAUTHORIZED, message, args);
    }
}
