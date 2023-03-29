package com.ecommerce.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.experimental.UtilityClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static com.ecommerce.error.CommonError.supplyErrorProcesses;

@UtilityClass
public class FormEmailSender {
    public static void formEmail(final JavaMailSender javaMailSender,
                                 final String emailUser,
                                 final String subject,
                                 final String contentForEmail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

            /* Set headers */
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            /* Create form */
            messageHelper.setFrom("gridshopvn@gmail.com", "GridShop - E-Commerce System");
            messageHelper.setTo(emailUser);

            messageHelper.setSubject(subject);
            messageHelper.setText(contentForEmail, true);
            javaMailSender.send(message);
        } catch (Exception ex) {
            throw supplyErrorProcesses("An error occurred while processing your request. Please try again later!").get();
        }
    }
}
