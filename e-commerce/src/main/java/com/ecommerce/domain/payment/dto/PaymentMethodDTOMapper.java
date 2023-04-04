package com.ecommerce.domain.payment.dto;

import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class PaymentMethodDTOMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static PaymentMethodDTO toPaymentMethodDTO(final PaymentMethodEntity paymentMethodEntity) {
        return modelMapper.map(paymentMethodEntity, PaymentMethodDTO.class);
    }

    public static PaymentMethodEntity toPaymentMethodEntity(final PaymentMethodDTO paymentMethodDTO) {
        return modelMapper.map(paymentMethodDTO, PaymentMethodEntity.class);
    }
}
