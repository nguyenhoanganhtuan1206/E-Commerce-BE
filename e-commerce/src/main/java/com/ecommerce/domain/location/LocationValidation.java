package com.ecommerce.domain.location;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.ecommerce.domain.location.LocationError.supplyLocationAlreadyExisted;

@UtilityClass
public class LocationValidation {

    public static void verifyIfLocationExisted(final List<LocationDTO> locationDTOS,
                                               final LocationRequestDTO locationRequestDTOs) {
        for (LocationDTO location : locationDTOS) {
            if (StringUtils.equals(location.getAddress(), locationRequestDTOs.getAddress()) &&
                    StringUtils.equals(location.getProvince(), locationRequestDTOs.getProvince()) &&
                    StringUtils.equals(location.getDistrict(), locationRequestDTOs.getCommune())) {
                throw supplyLocationAlreadyExisted().get();
            }
        }
    }
}
