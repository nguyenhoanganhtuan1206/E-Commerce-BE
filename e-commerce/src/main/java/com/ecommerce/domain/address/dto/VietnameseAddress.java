package com.ecommerce.domain.address.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class VietnameseAddress {

    private Set<Commune> communes;
    private Set<Province> provinces;
    private Set<District> districts;
}
