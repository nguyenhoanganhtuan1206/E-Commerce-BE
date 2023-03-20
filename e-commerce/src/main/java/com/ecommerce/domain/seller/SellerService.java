package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.auth.UserAuthenticationToken;
import com.ecommerce.domain.role.RoleDTO;
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
import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerCreateDTOMapper.toSellerDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerEntity;
import static com.ecommerce.utils.TokenGenerator.generateToken;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final AuthsProvider authsProvider;

    private final JavaMailSender javaMailSender;
    private final String URL_REGISTER = "http://localhost:8080/api/v1/seller/confirm-register?token=";

    public UserAuthenticationToken getCurrentUserToken() {
        return authsProvider.getCurrentAuthentication();
    }

    public SellerDTO findById(final UUID sellerId) {
        return toSellerDTO(sellerRepository.findById(sellerId)
                .orElseThrow(supplySellerNotFound(sellerId)));
    }

    public SellerDTO findByToken(final String token) {
        return toSellerDTO(sellerRepository.findByConfirmationToken(token)
                .orElseThrow(supplySellerNotFound(token)));
    }

    public SellerDTO registerSeller(final SellerCreateRequestDTO sellerRequestDTO) throws MessagingException, UnsupportedEncodingException {
        final SellerDTO seller = toSellerDTO(sellerRequestDTO);
        final UserDTO user = userService.findById(getCurrentUserToken().getUserId());
        SellerDTO registeredSeller;

        /**
         @  Check if the user has not yet registered as a seller.
         */
        if (isBlank(user.getSeller().getConfirmationToken())) {
            seller.setConfirmationToken(generateToken());
            seller.setSellerStatus(false);
            seller.setUser(user);

            registeredSeller = toSellerDTO(sellerRepository.save(toSellerEntity(seller)));
        } else {
            registeredSeller = user.getSeller();
        }

        final String registrationLink = URL_REGISTER + seller.getConfirmationToken();
        sendRegistrationEmail(user, seller.getEmailSeller(), registrationLink);

        return registeredSeller;
    }

    public SellerDTO confirmRegister(final String token) {
        final SellerDTO sellerDTO = findByToken(token);

        sellerDTO.setSellerStatus(true);
        sellerDTO.setConfirmationToken(null);

        final UserDTO userDTO = userService.findById(getCurrentUserToken().getUserId());
        final Set<RoleDTO> roleDTOS = userDTO.getRoles();
        roleDTOS.add(roleService.findByName("ROLE_SELLER"));
        sellerDTO.getUser().setRoles(roleDTOS);

        return toSellerDTO(sellerRepository.save(toSellerEntity(sellerDTO)));
    }

    private void sendRegistrationEmail(
            final UserDTO userDTO,
            final String emailSeller,
            final String registrationLink
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
        String content = "<body style='padding: 0;margin: 0;'>" +
                "    <div style='width: 600px;" +
                "    display: flex;" +
                "    justify-content: center;" +
                "    margin: auto;" +
                "    flex-direction: column;'>" +
                "        <div style='padding: 20px;border: 1px solid #dadada;'>" +
                "            <p style='font-size: 16px;color: #000;'>Dear, \"" + userDTO.getUsername() + "\"</p>" +
                "            <p style='font-size: 16px;color: #000;'>Confirm Your Registration as a Seller on Our E-commerce Platform.<br> <strong>Click the button" +
                "                below to confirm it </strong>. <br />If you did not request this registration, please ignore this email.</p>" +
                "            <a href='" + registrationLink + "' target=\"_self\" style=\"padding: 8px;border: none;display: block;cursor: pointer; border-radius: 3px;margin: 12px 0" +
                "            font-size: 15px;text-decoration: none;background-color: #0167f3;color: #fff;font-weight: 500;\">Confirm" +
                "                Registration</a>" +
                "            <p>Thank you for using our service.<br>" +
                "            <br>If you have any question, Please contact us immediately at <a href='mailto:gridshopvn@gmail.com@gmail.com' style='color: #0167f3;'>gridshopvn@gmail.com</a>" +
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
