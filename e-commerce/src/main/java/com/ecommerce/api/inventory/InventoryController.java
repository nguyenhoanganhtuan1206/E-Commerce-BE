package com.ecommerce.api.inventory;

import com.ecommerce.domain.inventory.InventoryService;
import com.ecommerce.domain.inventory.dto.InventoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.inventory.mapper.InventoryDTOMapper.toInventoryResponseDTO;

@RestController
@RequestMapping(value = "/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @RequestMapping(value = "{productId}/sizeValues")
    public List<String> findSizeValuesByColorValue(final @PathVariable UUID productId,
                                                   final @RequestParam(value = "colorValue") String colorValue) {
        return inventoryService.findSizeValuesByColorValueAndProductId(colorValue, productId);
    }

    @RequestMapping(value = "{productId}/colorValues")
    public List<String> findColorValuesBySizeValueAndProductId(final @PathVariable UUID productId,
                                                               final @RequestParam(value = "sizeValue") String sizeValue) {
        return inventoryService.findColorValuesBySizeValueAndProductId(sizeValue, productId);
    }

    @RequestMapping(value = "{productId}/inventoryDetail")
    public InventoryResponseDTO findInventoryDetailByParams(final @PathVariable UUID productId,
                                                            final @RequestParam(value = "sizeValue") String sizeValue,
                                                            final @RequestParam(value = "colorValue") String colorValue) {
        return toInventoryResponseDTO(inventoryService.findByColorValueAndSizeValueAndProductId(productId, sizeValue, colorValue));
    }
}
