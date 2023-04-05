package com.ecommerce.api.admin.seller;

import com.ecommerce.domain.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/admin/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PutMapping("/approval/{sellerId}")
    public void approvalForSeller(final @PathVariable UUID sellerId) {
        sellerService.approvalSeller(sellerId);
    }
}
