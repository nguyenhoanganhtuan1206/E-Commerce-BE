package com.ecommerce.api.seller;

import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.domain.seller.SellerDTO;
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
    @GetMapping("/registration")
    public SellerDTO getRegisteredSellerDetailsByUserId() {
        return sellerService.getRegisteredSellerDetailsByUserId();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/registration")
    public void registerNewSeller(
            final @Valid @RequestBody SellerSignUpRequestDTO sellerRequestDTO,
            final BindingResult bindingResult
    ) {
        handleValidationError(bindingResult);

        sellerService.registerNewSeller(sellerRequestDTO);
    }
}
