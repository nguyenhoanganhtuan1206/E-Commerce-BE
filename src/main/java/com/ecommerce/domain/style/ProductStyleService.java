package com.ecommerce.domain.style;

import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.seller.SellerService;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.style.ProductStyleEntity;
import com.ecommerce.persistent.style.ProductStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.style.ProductStyleError.supplyProductStyleExisted;
import static com.ecommerce.domain.style.ProductStyleError.supplyProductStyleNotFound;

@Service
@RequiredArgsConstructor
public class ProductStyleService {

    private final ProductStyleRepository productStyleRepository;

    private final SellerService sellerService;

    private final AuthsProvider authsProvider;

    public ProductStyleEntity findByName(final String name) {
        return productStyleRepository.findByName(name)
                .orElseThrow(supplyProductStyleNotFound("name", name));
    }

    public List<ProductStyleEntity> findAllWithoutSellerId() {
        return productStyleRepository.findAllWithoutSellerId();
    }

    public ProductStyleEntity createProductStyle(final ProductStyleEntity productStyle) {
        final SellerEntity sellerFound = sellerService.findByUserId(authsProvider.getCurrentUserId());
        verifyIfProductStyleAvailable(productStyle.getName(), sellerFound.getId());

        final ProductStyleEntity productCreate = ProductStyleEntity.builder()
                .name(productStyle.getName())
                .seller(sellerFound)
                .build();

        return productStyleRepository.save(productCreate);
    }

    public List<ProductStyleEntity> findBySellerId() {
        final SellerEntity sellerFound = sellerService.findByUserId(authsProvider.getCurrentUserId());

        return productStyleRepository.findBySellerId(sellerFound.getId());
    }

    public void deleteByNameAndSellerId(final String name) {
        final ProductStyleEntity productStyle = findByName(name);
        productStyleRepository.delete(productStyle);
    }

    private void verifyIfProductStyleAvailable(final String productStyleName, final UUID sellerId) {
        /* Check on system whether style is available or not? */
        productStyleRepository.findByName(productStyleName)
                .ifPresent(productStyleEntity -> {
                    throw supplyProductStyleExisted("name", productStyleName).get();
                });
        productStyleRepository.findByNameAndSellerId(productStyleName, sellerId)
                .ifPresent(productStyleEntity -> {
                    throw supplyProductStyleExisted("name", productStyleName).get();
                });
    }
}
