package com.ecommerce.domain.inventory;

import com.ecommerce.api.inventory.InventoryCreateRequestDTO;
import lombok.experimental.UtilityClass;

import static com.ecommerce.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class InventoryValidation {

    public static void validateInventoryInformationNotEmpty(final InventoryCreateRequestDTO inventoryRequestDTO) {
        if (isBlank(inventoryRequestDTO.getColorName())) {
            throw supplyValidationError("Color name cannot be empty.").get();
        }

        if (isBlank(inventoryRequestDTO.getColorValue())) {
            throw supplyValidationError("Color value cannot be empty.").get();
        }

        if (isBlank(inventoryRequestDTO.getSizeName())) {
            throw supplyValidationError("Size name cannot be empty.").get();
        }

        if (isBlank(inventoryRequestDTO.getSizeValue())) {
            throw supplyValidationError("Size value cannot be empty.").get();
        }

        if (inventoryRequestDTO.getPrice() < 0 || inventoryRequestDTO.getPrice() > 1000000) {
            throw supplyValidationError("Price must be between 0 and 1000000.").get();
        }

        if (inventoryRequestDTO.getQuantity() > 10000) {
            throw supplyValidationError("Quantity cannot large than 10000 unit.").get();
        }
    }
}
