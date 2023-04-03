package com.ecommerce.api.seller;

import com.ecommerce.api.seller.dto.SellerConfirmRequestDTO;
import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.seller.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/confirm-register")
    public void requestConfirmationEmailForSale(
            final @Valid @RequestBody SellerConfirmRequestDTO sellerConfirmRequestDTO,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        sellerService.requestConfirmationEmailForSale(sellerConfirmRequestDTO.getEmailSeller());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public void registerNewSeller(
            final @RequestParam("token") String token,
            final @Valid @RequestBody SellerCreateRequestDTO sellerCreateRequestDTO,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        sellerService.registerNewSeller(token, sellerCreateRequestDTO);
    }
}
