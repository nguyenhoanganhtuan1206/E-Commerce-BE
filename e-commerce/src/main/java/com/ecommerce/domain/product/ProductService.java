package com.ecommerce.domain.product;

import com.ecommerce.api.product.dto.ProductCreateRequestDTO;
import com.ecommerce.api.product.dto.ProductResponseDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ecommerce.domain.product.mapper.ProductCreateMapper.toProductEntity;
import static com.ecommerce.domain.product.mapper.ProductCreateMapper.toProductResponseDTO;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserService userService;

    private final ProductRepository productRepository;

    public ProductResponseDTO create(
            final UUID userId,
            final ProductCreateRequestDTO productCreateRequestDTO
    ) {
        final UserDTO userDTO = userService.findById(userId);



        return toProductResponseDTO(productRepository.save(toProductEntity(productCreateRequestDTO)));
    }
}
