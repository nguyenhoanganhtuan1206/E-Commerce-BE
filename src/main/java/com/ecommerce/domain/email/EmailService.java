package com.ecommerce.domain.email;

import com.ecommerce.persistent.product.ProductEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Map;

import static com.ecommerce.utils.ResourceUtils.readResource;
import static com.ecommerce.utils.StringUtils.findCaptureGroup;
import static com.ecommerce.utils.StringUtils.replaceText;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String APPROVE_PRODUCT_TEMPLATE_PATH = "/templates/email/ApproveProduct.html";

    private static final String FEEDBACK_PRODUCT_TEMPLATE_PATH = "/templates/email/FeedbackProduct.html";

    private static final String FORGET_PASSWORD_TEMPLATE_PATH = "/templates/email/ForgetPasswordEmail.html";

    private static final String EMAIL_SUBJECT_REGEX = "<title>(.*?)</title>";

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String contactMail;

    @Value("${app.system.name}")
    private String systemName;

    public void sendApproveProductEmail(final SellerEntity seller, final ProductEntity product) {
        final String email = seller.getEmailSeller();
        final String mailContent = readResource(APPROVE_PRODUCT_TEMPLATE_PATH);
        final Map<String, String> replacements = createReplacementsMapApproveProduct(seller, product);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void sendEmailFeedbackProduct(final SellerEntity seller, final ProductEntity product, final String contentFeedback) {
        final String email = seller.getEmailSeller();
        final String mailContent = readResource(FEEDBACK_PRODUCT_TEMPLATE_PATH);
        final Map<String, String> replacements = createReplacementsMapFeedbackProduct(seller, product, contentFeedback);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(email, formattedMailContent));
    }

    public void sendEmailForgetPassword(final UserEntity user, final String linkResetPassword) {
        final String mailContent = readResource(FORGET_PASSWORD_TEMPLATE_PATH);
        final Map<String, String> replacements = createReplacementsMapForgetPassword(user, linkResetPassword);
        final String formattedMailContent = replacePlaceholders(mailContent, replacements);

        mailSender.send(buildMessage(user.getEmail(), formattedMailContent));
    }

    private Map<String, String> createReplacementsMapApproveProduct(final SellerEntity seller, final ProductEntity product) {
        return Map.of(
                "username", seller.getUser().getUsername(),
                "productName", product.getName()
        );
    }

    private Map<String, String> createReplacementsMapFeedbackProduct(final SellerEntity seller, final ProductEntity product, final String contentFeedback) {
        return Map.of(
                "username", seller.getUser().getUsername(),
                "productName", product.getName(),
                "contentFeedback", contentFeedback
        );
    }

    private Map<String, String> createReplacementsMapForgetPassword(final UserEntity user, final String linkResetPassword) {
        return Map.of(
                "username", user.getUsername(),
                "linkResetPassword", linkResetPassword
        );
    }

    private String replacePlaceholders(final String mailContent, final Map<String, String> replacements) {
        return replaceText(mailContent, replacements);
    }

    private MimeMessage buildMessage(final String sendTo, final String body) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            final String subject = findCaptureGroup(body, EMAIL_SUBJECT_REGEX);

            messageHelper.setFrom(contactMail, systemName);
            messageHelper.setTo(sendTo);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);

            return message;
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while processing the email. Please try again later", ex);
        }
    }
}
