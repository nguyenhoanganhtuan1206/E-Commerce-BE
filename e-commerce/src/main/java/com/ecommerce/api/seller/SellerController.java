package com.ecommerce.api.seller;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.domain.seller.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.api.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTO;
import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/registration")
    public SellerResponseDTO getRegisteredSellerDetailsByUserId() {
        if (sellerService.getRegisteredSellerDetailsByUserId().isPresent()) {
            return toSellerResponseDTO(sellerService.getRegisteredSellerDetailsByUserId().get());
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/registration")
    public SellerResponseDTO registerNewSeller(final @Valid @RequestBody SellerSignUpRequestDTO sellerRequestDTO, final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return sellerService.registerNewSeller(sellerRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/registration")
    public SellerResponseDTO updateSeller(final @Valid @RequestBody SellerSignUpRequestDTO sellerSignUpRequestDTO,
                                          final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        return sellerService.updateSeller(sellerSignUpRequestDTO);
    }
}
