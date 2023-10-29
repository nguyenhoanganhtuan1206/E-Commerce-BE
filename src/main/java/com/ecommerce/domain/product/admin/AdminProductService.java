package com.ecommerce.domain.product.admin;

import com.ecommerce.domain.email.EmailService;
import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import com.ecommerce.persistent.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;

    private final CommonProductService commonProductService;

    private final EmailService emailService;

    public ProductEntity approvalProduct(final UUID productId) {
        final ProductEntity currentProduct = commonProductService.findById(productId);
        currentProduct.setProductApproval(Status.ACTIVE);

        emailService.sendApproveProductEmail(currentProduct.getSeller(), currentProduct);
        return productRepository.save(currentProduct);
    }

    public ProductEntity deActivateProduct(final UUID productId) {
        final ProductEntity currentProduct = commonProductService.findById(productId);
        currentProduct.setProductApproval(Status.DEACTIVATE);

        return productRepository.save(currentProduct);
    }

    public void sendFeedbackAboutProductToUser(final UUID productId, final String contentFeedback) {
        final ProductEntity product = commonProductService.findById(productId);

        emailService.sendEmailFeedbackProduct(product.getSeller(), product, contentFeedback);
    }
}
