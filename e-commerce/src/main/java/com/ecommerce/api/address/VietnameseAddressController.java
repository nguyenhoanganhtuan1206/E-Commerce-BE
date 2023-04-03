package com.ecommerce.api.address;

import com.ecommerce.domain.address.AddressService;
import com.ecommerce.domain.address.dto.Commune;
import com.ecommerce.domain.address.dto.District;
import com.ecommerce.domain.address.dto.Province;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/address")
@RequiredArgsConstructor
public class VietnameseAddressController {

    private final AddressService addressService;

    @GetMapping
    public Set<Province> findProvinces() {
        return addressService.findProvinces();
    }

    @GetMapping("/districts/{provinceId}/province")
    public Set<District> findDistrictByProvinceId(final @PathVariable String provinceId) {
        return addressService.findDistrictByProvinceId(provinceId);
    }

    @GetMapping("/communes/{districtId}/district")
    public Set<Commune> findCommuneByDistrictId(final @PathVariable String districtId) {
        return addressService.findCommuneByDistrictId(districtId);
    }
}
