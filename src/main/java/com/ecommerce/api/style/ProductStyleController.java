package com.ecommerce.api.style;

import com.ecommerce.api.style.dto.ProductStyleRequestDTO;
import com.ecommerce.api.style.dto.ProductStyleResponseDTO;
import com.ecommerce.domain.style.ProductStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ecommerce.domain.style.mapper.ProductStyleDTOMapper.*;

@RestController
@RequestMapping(value = "/api/v1/product-styles")
@RequiredArgsConstructor
public class ProductStyleController {

    private final ProductStyleService productStyleService;

    @GetMapping
    public List<ProductStyleResponseDTO> findAllWithoutSellerId() {
        return toProductStyleResponseDTOs(productStyleService.findAllWithoutSellerId());
    }

    @PostMapping
    public ProductStyleResponseDTO createProductStyle(@RequestBody final ProductStyleRequestDTO productStyleRequestDTO) {
        return toProductStyleResponseDTO(productStyleService.createProductStyle(toProductStyleEntity(productStyleRequestDTO)));
    }

    @GetMapping("/seller")
    public List<ProductStyleResponseDTO> findBySellerId() {
        return toProductStyleResponseDTOs(productStyleService.findBySellerId());
    }

    @DeleteMapping
    public void deleteBySellerId(@RequestParam final String styleName) {
        productStyleService.deleteByNameAndSellerId(styleName);
    }
}
