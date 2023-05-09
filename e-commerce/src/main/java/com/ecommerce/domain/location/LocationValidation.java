package com.ecommerce.domain.location;

import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.persistent.location.LocationEntity;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.ecommerce.domain.location.LocationError.supplyLocationAlreadyExisted;

@UtilityClass
public class LocationValidation {

    public static void verifyIfLocationExisted(final List<LocationEntity> locationsExisted,
                                               final LocationRequestDTO locationRequestDTOs) {
        for (LocationEntity location : locationsExisted) {
            if (StringUtils.equals(location.getAddress(), locationRequestDTOs.getAddress()) &&
                    StringUtils.equals(location.getProvince(), locationRequestDTOs.getProvince()) &&
                    StringUtils.equals(location.getDistrict(), locationRequestDTOs.getCommune())) {
                throw supplyLocationAlreadyExisted().get();
            }
        }
    }
}
