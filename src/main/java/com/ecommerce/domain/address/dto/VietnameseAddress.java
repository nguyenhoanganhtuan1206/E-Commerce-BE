package com.ecommerce.domain.address.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VietnameseAddress {

    private List<Commune> communes;
    private List<Province> provinces;
    private List<District> districts;
}
