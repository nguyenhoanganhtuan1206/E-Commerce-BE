package com.ecommerce.domain.imagesProduct;

import com.ecommerce.persistent.imagesProduct.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.imagesProduct.mapper.ImagesDTOMapper.toImagesEntity;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepository imagesRepository;

    public void save(final ImagesDTO imagesDTO) {
        imagesRepository.save(toImagesEntity(imagesDTO));
    }
}
