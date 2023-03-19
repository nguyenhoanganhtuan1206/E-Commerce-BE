package com.ecommerce.api.seller;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.domain.seller.SellerService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("{userId}")
    public SellerDTO registerForSell(
            final @PathVariable UUID userId,
            final @Valid @RequestBody SellerCreateRequestDTO sellerCreateRequestDTO,
            final BindingResult bindingResult
    ) throws MessagingException, UnsupportedEncodingException {
        handleValidationError(bindingResult);

        return sellerService.registerForSell(userId, sellerCreateRequestDTO);
    }

    @PutMapping("{userId}/confirm-register")
    public SellerDTO sellConfirm(
            final @PathVariable UUID userId
    ) {
        return sellerService.sellConfirm(userId);
    }
}
