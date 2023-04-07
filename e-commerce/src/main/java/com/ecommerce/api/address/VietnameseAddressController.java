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

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/address")
@RequiredArgsConstructor
public class VietnameseAddressController {

    private final AddressService addressService;

    @GetMapping
    public List<Province> findProvinces() {
        return addressService.findProvinces();
    }

    @GetMapping("/districts/{provinceName}/province")
    public List<District> findDistrictByProvinceId(final @PathVariable String provinceName) {
        return addressService.findDistrictByProvinceName(provinceName);
    }

    @GetMapping("/communes/{districtName}/district")
    public List<Commune> findCommuneByDistrictId(final @PathVariable String districtName) {
        return addressService.findCommuneByDistrictName(districtName);
    }
}
