package com.ecommerce.domain.product.admin;

import com.ecommerce.domain.product.CommonProductService;
import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.product.ProductRepository;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static com.ecommerce.utils.FormEmailSender.formEmail;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;

    private final CommonProductService commonProductService;

    private final JavaMailSender javaMailSender;

    public ProductEntity approvalProduct(final UUID productId) {
        final ProductEntity currentProduct = commonProductService.findById(productId);
        currentProduct.setProductApproval(Status.ACTIVE);

        sendEmailApproveProduct(currentProduct.getSeller(), currentProduct);
        return productRepository.save(currentProduct);
    }

    public ProductEntity deActivateProduct(final UUID productId, final String contentFeedback) {
        final ProductEntity currentProduct = commonProductService.findById(productId);
        currentProduct.setProductApproval(Status.DEACTIVATE);

        sendEmailFeedbackProduct(currentProduct.getSeller(), currentProduct, contentFeedback);
        return productRepository.save(currentProduct);
    }

    public void sendFeedbackAboutProductToUser(final UUID productId, final String contentFeedback) {
        final ProductEntity product = commonProductService.findById(productId);

        sendEmailFeedbackProduct(product.getSeller(), product, contentFeedback);
    }

    private void sendEmailApproveProduct(final SellerEntity seller, final ProductEntity product) {
        try {
            final String subject = "[Grid Shop] - Product Approval Notification";
            final String content = "<html>\n" +
                    "  <body style=\"padding: 0; margin: 0;\">\n" +
                    "    <div style=\"width: 600px; margin: auto;\">\n" +
                    "      <div style=\"padding: 20px; border: 1px solid #dadada;\">\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">Greetings, <strong>" + seller.getUser().getUsername() + "</strong>,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">\n" +
                    "          We would like to inform you that your product <strong>" + product.getName() + "</strong> has been approved by our system.\n" +
                    "        </p>\n" +
                    "        <br>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">Thank you for using our service.</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">If you have any questions, please don't hesitate to contact us immediately at <a href=\"mailto:gridshopvn@gmail.com\">gridshopvn@gmail.com</a>.</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">Thank you,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">The Grid Shop Team</p>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";

            formEmail(javaMailSender, seller.getEmailSeller(), subject, content);
        } catch (Exception ex) {
            throw supplyErrorProcesses("An error occurred while processing your request. Please try again later!").get();
        }
    }

    private void sendEmailFeedbackProduct(final SellerEntity seller, final ProductEntity product, final String contentFeedback) {
        try {
            final String subject = "[Grid Shop] - Product Compliance Notification";
            final String content = "<html>\n" +
                    "  <body style=\"padding: 0; margin: 0;\">\n" +
                    "    <div style=\"width: 600px; margin: auto;\">\n" +
                    "      <div style=\"padding: 20px; border: 1px solid #dadada;\">\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">Greetings, <strong>" + seller.getUser().getUsername() + "</strong>,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">\n" +
                    "          We would like to inform you that your product \"" + product.getName() + "\" does not comply with our system's standards.\n" +
                    "        </p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">\n" +
                    "          To ensure a quality experience for our users, we kindly request you to make the necessary adjustments to bring your product into compliance.\n" +
                    "        </p>\n" +
                    "        <br>\n" +
                    "        <strong>Reason for non-compliance:</strong>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">\n" +
                    "          " + contentFeedback + "\n" +
                    "        </p>\n" +
                    "        <br>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">Thank you for your attention to this matter. If you have any questions or need further assistance, please don't hesitate to contact us immediately at <a href=\"mailto:gridshopvn@gmail.com\">gridshopvn@gmail.com</a>.</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">Thank you,</p>\n" +
                    "        <p style=\"font-size: 16px; color: #000;\">The Grid Shop Team</p>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";

            formEmail(javaMailSender, seller.getEmailSeller(), subject, content);
        } catch (Exception ex) {
            throw supplyErrorProcesses("An error occurred while processing your request. Please try again later!").get();
        }
    }
}
