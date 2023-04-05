package com.ecommerce.api.seller;

import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.api.seller.dto.SellerSignUpResponseDTO;
import com.ecommerce.domain.seller.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.api.seller.mapper.SellerSignUpDTOMapper.toSellerSignUpResponseDTO;
import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/registration")
    public SellerSignUpResponseDTO getRegisteredSellerDetailsByUserId() {
        if (sellerService.getRegisteredSellerDetailsByUserId().isPresent()) {
            return toSellerSignUpResponseDTO(sellerService.getRegisteredSellerDetailsByUserId().get());
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/registration")
    public void updateSeller(final @Valid @RequestBody SellerSignUpRequestDTO sellerSignUpRequestDTO,
                             final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        sellerService.updateSeller(sellerSignUpRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/registration")
    public void registerNewSeller(final @Valid @RequestBody SellerSignUpRequestDTO sellerRequestDTO, final BindingResult bindingResult) {
        handleValidationError(bindingResult);

        sellerService.registerNewSeller(sellerRequestDTO);
    }
}
