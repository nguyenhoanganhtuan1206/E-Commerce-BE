package com.ecommerce.api.brand.mapper;

import com.ecommerce.api.brand.dto.BrandResponseDTO;
import com.ecommerce.domain.brand.BrandDTO;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class BrandDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static BrandResponseDTO toBrandResponseDTO(final BrandDTO brandDTO) {
        return modelMapper.map(brandDTO, BrandResponseDTO.class);
    }

    public static Set<BrandResponseDTO> toBrandResponseDTOs(final Set<BrandDTO> brandDTOs) {
        return brandDTOs.stream()
                .map(BrandDTOMapper::toBrandResponseDTO)
                .collect(Collectors.toSet());
    }
}
