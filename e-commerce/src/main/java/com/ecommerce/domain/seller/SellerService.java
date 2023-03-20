package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.seller.SellerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerCreateDTOMapper.toSellerEntity;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerEntity;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final JavaMailSender javaMailSender;

    private final String URL_REGISTER = "http://localhost:8080/api/v1/seller/";

    public SellerDTO findById(final UUID sellerId) {
        return toSellerDTO(sellerRepository.findById(sellerId)
                .orElseThrow(supplySellerNotFound(sellerId)));
    }

    public SellerDTO findByUserId(final UUID userId) {
        return toSellerDTO(sellerRepository.findByUserId(userId)
                .orElseThrow(supplySellerNotFound(userId)));
    }

    public SellerDTO registerForSell(
            final UUID userId,
            final SellerCreateRequestDTO sellerDTO
    ) throws MessagingException, UnsupportedEncodingException {
        final UserDTO userDTO = userService.findById(userId);

        final String linkToRegister = "http://localhost:8080/api/v1/seller/" + userDTO.getId() + "/confirm-register";
        handleSendEmail(userDTO, sellerDTO.getEmailSeller(), linkToRegister);

        sellerDTO.setSellerStatus(false);
        sellerDTO.setUser(userDTO);

        return toSellerDTO(sellerRepository.save(toSellerEntity(sellerDTO)));
    }

    public SellerDTO sellConfirm(final UUID userId) {
        userService.findById(userId)
                .getRoles()
                .add(roleService.findByName("ROLE_SELLER"));

        final SellerDTO sellerDTO = findByUserId(userId);

        sellerDTO.setSellerStatus(true);
        return toSellerDTO(sellerRepository.save(toSellerEntity(sellerDTO)));
    }

    private void handleSendEmail(
            final UserDTO userDTO,
            final String emailSeller,
            final String linkToRegister
    ) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

        /* Set headers */
        message.addHeader("Content-type", "text/HTML; charset=UTF-8");
        message.addHeader("format", "flowed");
        message.addHeader("Content-Transfer-Encoding", "8bit");

        /* Create form */
        messageHelper.setFrom("gridshopvn@gmail.com", "GridShop - E-Commerce System");
        messageHelper.setTo(emailSeller);

        String subject = "Confirm Your Registration as a Seller on Our E-commerce Platform";
        String content = "<body style=\"padding: 0;margin: 0;\">" +
                "    <div style=\"width: 600px;" +
                "    display: flex;" +
                "    justify-content: center;" +
                "    margin: auto;" +
                "    flex-direction: column;\">" +
                "        <div style=\"padding: 20px;border: 1px solid #dadada;\">" +
                "            <p style=\"font-size: 16px;color: #000;\">Dear, \"" + userDTO.getUsername() + "\"</p>" +
                "            <p style=\"font-size: 16px;color: #000;\">Confirm Your Registration as a Seller on Our E-commerce Platform.<br> <strong>Click the button" +
                "                below to confirm it </strong>. <br />If you did not request this registration, please ignore this email.</p>" + "            " +
                "           <form method=\"PUT\" action=\"" + linkToRegister + "\">" +
                "                <button type=\"submit\" style=\"padding: 8px;display: inline-block;cursor: pointer; border-radius: 3px;" +
                "                    font-size: 15px;text-decoration: none;background-color: #0167f3;color: #fff;font-weight: 500;\">" +
                "                    Confirm Registration" +
                "                </button>" +
                "            </form>" +
                "            Thank you for using our service.<br>" +
                "            <br>If you have any question, Please contact us immediately at <a href=\"mailto:gridshopvn@gmail.com@gmail.com\">gridshopvn@gmail.com</a>" +
                "            </p>" +
                "            <p>Thanks you.</p>" +
                "            <p>Grid Shop Team.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>";

        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        javaMailSender.send(message);
    }
}
