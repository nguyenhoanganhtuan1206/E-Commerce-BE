package com.ecommerce.api.admin.seller;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.domain.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ecommerce.api.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTOs;

@RestController
@RequestMapping(value = "/api/v1/admin/sellers")
@RequiredArgsConstructor
public class SellerControllerAdmin {

    private final SellerService sellerService;

    @GetMapping
    public List<SellerResponseDTO> findAll() {
        return toSellerResponseDTOs(sellerService.findAllSortedByCreatedAt());
    }
}
