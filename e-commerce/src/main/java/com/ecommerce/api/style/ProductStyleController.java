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
    public List<ProductStyleResponseDTO> findAll() {
        return toProductStyleResponseDTOs(productStyleService.findAll());
    }

    @PostMapping
    public ProductStyleResponseDTO createProductStyle(@RequestBody final ProductStyleRequestDTO productStyleRequestDTO) {
        return toProductStyleResponseDTO(productStyleService.createProductStyle(toProductStyleEntity(productStyleRequestDTO)));
    }
}
