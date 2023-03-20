package com.ecommerce.api.seller;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.seller.SellerService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public SellerDTO registerForSell(
            final @Valid @RequestBody SellerCreateRequestDTO sellerCreateRequestDTO,
            final BindingResult bindingResult
    ) throws MessagingException, UnsupportedEncodingException {
        handleValidationError(bindingResult);

        return sellerService.registerSeller(sellerCreateRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/confirm-register")
    public SellerDTO confirmRegister(
            final @RequestParam(name = "token") String token
    ) {
        return sellerService.confirmRegister(token);
    }
}
