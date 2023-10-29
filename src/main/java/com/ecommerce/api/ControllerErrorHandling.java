package com.ecommerce.api;

import com.ecommerce.error.DomainException;
import com.ecommerce.error.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandling {

    @ExceptionHandler({DomainException.class})
    public ResponseEntity<ErrorDTO> handleDomainError(final DomainException error) {
        final var errorDTO = ErrorDTO.builder()
                .message(error.getMessage())
                .occurAt(Instant.now())
                .build();

        return ResponseEntity
                .status(error.getHttpStatus())
                .body(errorDTO);
    }
}
