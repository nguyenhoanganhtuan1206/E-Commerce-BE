package com.ecommerce.api.admin.seller;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.domain.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ecommerce.domain.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTO;
import static com.ecommerce.domain.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTOs;

@RestController
@RequestMapping(value = "/api/v1/admin/sellers")
@RequiredArgsConstructor
public class SellerControllerAdmin {

    private final SellerService sellerService;

    @GetMapping
    public List<SellerResponseDTO> findAll() {
        return toSellerResponseDTOs(sellerService.findAllSortedByCreatedAt());
    }

    @GetMapping("{sellerId}")
    public SellerResponseDTO findById(final @PathVariable UUID sellerId) {
        return toSellerResponseDTO(sellerService.findById(sellerId));
    }

    @PutMapping("{sellerId}/approval")
    public SellerResponseDTO approveSellerRequest(final @PathVariable UUID sellerId) {
        return toSellerResponseDTO(sellerService.approveSellerRequest(sellerId));
    }

    @PostMapping("{sellerId}/feedback")
    public void sendFeedbackToUser(final @PathVariable UUID sellerId, final @RequestBody Map<String, String> requestBody) {
        sellerService.sendFeedbackToUser(sellerId, requestBody.get("contentFeedback"));
    }
}
