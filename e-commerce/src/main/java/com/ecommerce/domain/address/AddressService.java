package com.ecommerce.domain.address;

import com.ecommerce.domain.address.dto.Commune;
import com.ecommerce.domain.address.dto.District;
import com.ecommerce.domain.address.dto.Province;
import com.ecommerce.domain.address.dto.VietnameseAddress;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

    public List<Province> findProvinces() {
        return vietnameseAddress.getProvinces();
    }

    public List<District> findDistrictByProvinceName(final String provinceName) {
        final Province province = vietnameseAddress.getProvinces().stream()
                .filter(p -> p.getName().equals(provinceName))
                .findFirst()
                .orElseThrow(supplyAddressNotFound(format("Cannot found province with %s", provinceName)));

        return vietnameseAddress.getDistricts().stream()
                .filter(d -> d.getIdProvince().equals(province.getIdProvince()))
                .toList();
    }

    public List<Commune> findCommuneByDistrictName(final String districtName) {
        final District district = vietnameseAddress.getDistricts().stream()
                .filter(d -> d.getName().equals(districtName))
                .findFirst()
                .orElseThrow(supplyAddressNotFound(format("Cannot found district with %s", districtName)));

        return vietnameseAddress.getCommunes().stream()
                .filter(c -> c.getIdDistrict().equals(district.getIdDistrict()))
                .toList();
    }
}
