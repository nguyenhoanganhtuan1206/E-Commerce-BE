package com.ecommerce.domain.seller.mapper;

import com.ecommerce.domain.product.ProductDTO;
import com.ecommerce.domain.seller.SellerDTO;
import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

@UtilityClass
public class SellerDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static SellerDTO toSellerDTO(final SellerEntity sellerEntity) {
        final SellerDTO sellerDTO = modelMapper.map(sellerEntity, SellerDTO.class);

        if (sellerDTO.getProducts() != null) {
            sellerDTO.setProducts(sellerEntity.getProducts().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toSet()));
        }

        return sellerDTO;
    }

    public static SellerEntity toSellerEntity(final SellerDTO sellerDTO) {
        final SellerEntity sellerEntity = modelMapper.map(sellerDTO, SellerEntity.class);

        if (sellerEntity.getProducts() != null) {
            sellerEntity.setProducts(sellerDTO.getProducts().stream()
                    .map(product -> modelMapper.map(product, ProductEntity.class))
                    .collect(Collectors.toSet()));
        }

        sellerEntity.setPaymentMethods(sellerDTO.getPaymentMethodDTOs().stream()
                .map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodEntity.class))
                .collect(Collectors.toSet()));

        return sellerEntity;
    }
}
