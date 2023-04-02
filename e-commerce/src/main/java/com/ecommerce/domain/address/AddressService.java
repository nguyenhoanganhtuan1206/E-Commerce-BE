package com.ecommerce.domain.address;

import com.ecommerce.domain.address.dto.Commune;
import com.ecommerce.domain.address.dto.District;
import com.ecommerce.domain.address.dto.Province;
import com.ecommerce.domain.address.dto.VietnameseAddress;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ecommerce.domain.address.AddressError.supplyAddressNotFound;
import static com.ecommerce.utils.JsonParser.parseJson;
import static java.lang.String.format;

@Service
public class AddressService {

    final String filePath = "data/regionData.json";

    private final VietnameseAddress vietnameseAddress;

    public AddressService() throws IOException {
        vietnameseAddress = parseJson(VietnameseAddress.class, filePath);
    }

    public Province findProvinceById(final String provinceId) {
        return vietnameseAddress.getProvinces().stream()
                .filter(p -> p.getIdProvince().equals(provinceId))
                .findFirst()
                .orElseThrow(supplyAddressNotFound(format("Cannot found province with %s", provinceId)));
    }

    public Set<Province> findProvinces() {
        return vietnameseAddress.getProvinces();
    }

    public Set<District> findDistrictByProvinceId(final String provinceId) {
        return vietnameseAddress.getDistricts().stream()
                .filter(d -> d.getIdProvince().equals(provinceId))
                .collect(Collectors.toSet());
    }

    public Set<Commune> findCommuneByDistrictId(final String districtId) {
        return vietnameseAddress.getCommunes().stream()
                .filter(c -> c.getIdDistrict().equals(districtId))
                .collect(Collectors.toSet());
    }
}
