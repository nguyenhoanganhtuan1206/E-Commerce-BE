package com.ecommerce.domain.inventory;

import com.ecommerce.api.inventory.InventoryCreateRequestDTO;
import lombok.experimental.UtilityClass;

import static com.ecommerce.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@UtilityClass
public class InventoryValidation {

    public static void validateInventoryInformationNotEmpty(final InventoryCreateRequestDTO inventoryRequestDTO) {
        if (isBlank(inventoryRequestDTO.getColorName()) && isNotBlank(inventoryRequestDTO.getColorValue())) {
            throw supplyValidationError("Color name cannot be empty.").get();
        }

        if (isBlank(inventoryRequestDTO.getSizeName()) && isNotBlank(inventoryRequestDTO.getSizeValue())) {
            throw supplyValidationError("Size name cannot be empty.").get();
        }

        if (inventoryRequestDTO.getPrice() < 0 || inventoryRequestDTO.getPrice() > 1000000) {
            throw supplyValidationError("Price must be between 0 and 1000000.").get();
        }

        if (inventoryRequestDTO.getQuantity() > 10000) {
            throw supplyValidationError("Quantity cannot large than 10000 unit.").get();
        }
    }
}
