package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.seller.mapper.SellerDTOMapper;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.seller.SellerRepository;
import com.ecommerce.persistent.status.Status;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecommerce.api.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTO;
import static com.ecommerce.domain.seller.SellerError.supplyEmailSellerUsedError;
import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.*;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static com.ecommerce.utils.FormEmailSender.formEmail;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final PaymentMethodService paymentMethodService;

    private final AuthsProvider authsProvider;

    private final JavaMailSender javaMailSender;

    public List<SellerDTO> findAllSortedByCreatedAt() {
        return toSellerDTOs(sellerRepository.findAllSortedByCreatedAt());
    }

    public SellerDTO findById(final UUID sellerId) {
        return toSellerDTO(sellerRepository.findById(sellerId)
                .orElseThrow(supplySellerNotFound("id", sellerId.toString())));
    }

    public SellerEntity findByUserId(final UUID userId) {
        return sellerRepository.findByUserId(userId)
                .orElseThrow(supplySellerNotFound("user id", userId.toString()));
    }

    public Optional<SellerDTO> getRegisteredSellerDetailsByUserId() {
        return sellerRepository.findByUserId(authsProvider.getCurrentUserId())
                .map(SellerDTOMapper::toSellerDTO);
    }

    public SellerResponseDTO registerNewSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        verifyPermissionSellerRegister(userDTO);

        return toSellerResponseDTO(createNewSeller(sellerRequestDTO, userDTO));
    }

    public SellerResponseDTO updateSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final SellerDTO sellerDTO = toSellerDTO(findByUserId(authsProvider.getCurrentUserId()));
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        if (!StringUtils.equals(sellerRequestDTO.getEmailSeller(), sellerDTO.getEmailSeller())) {
            verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        }
        verifyPermissionSellerRegister(userDTO);
        return toSellerResponseDTO(updateSeller(sellerDTO, sellerRequestDTO));
    }

    public void sendFeedbackToUser(final UUID sellerId, final String contentFeedback) {
        final String BASE_URL_REGISTER_FORM = "http://localhost:3000/registration-seller";
        final SellerDTO sellerDTO = findById(sellerId);

        sendEmailFeedbackUser(sellerDTO, BASE_URL_REGISTER_FORM, contentFeedback);
    }

    public SellerDTO approveSellerRequest(final UUID sellerId) {
        final SellerDTO sellerDTO = findById(sellerId);
        final UserDTO userDTO = sellerDTO.getUser();

        userDTO.getRoles().add(roleService.findByName("ROLE_SELLER"));
        sellerDTO.setSellerApproval(Status.ACTIVE);

        userService.save(userDTO);
        return toSellerDTO(sellerRepository.save(toSellerEntity(sellerDTO)));
    }

    private SellerDTO createNewSeller(final SellerSignUpRequestDTO sellerRequestDTO, final UserDTO userDTO) {
        final Set<PaymentMethodDTO> paymentMethodDTOs = findPaymentMethodsByName(sellerRequestDTO.getNamePaymentMethods());

        final SellerDTO sellerDTO = SellerDTO.builder()
                .sellerName(sellerRequestDTO.getSellerName())
                .emailSeller(sellerRequestDTO.getEmailSeller())
                .sellerRating((float) 0)
                .phoneNumber(sellerRequestDTO.getPhoneNumber())
                .province(sellerRequestDTO.getProvince())
                .district(sellerRequestDTO.getDistrict())
                .commune(sellerRequestDTO.getCommune())
                .address(sellerRequestDTO.getAddress())
                .sellerApproval(Status.PENDING)
                .user(userDTO)
                .createdAt(Instant.now())
                .paymentMethods(paymentMethodDTOs)
                .build();

        return toSellerDTO(sellerRepository.save(toSellerEntity(sellerDTO)));
    }

    private SellerDTO updateSeller(final SellerDTO sellerDTO, final SellerSignUpRequestDTO sellerRequestDTO) {
        final Set<PaymentMethodDTO> paymentMethodDTOs = findPaymentMethodsByName(sellerRequestDTO.getNamePaymentMethods());

        sellerDTO.setSellerName(sellerRequestDTO.getSellerName());
        sellerDTO.setEmailSeller(sellerRequestDTO.getEmailSeller());
        sellerDTO.setSellerRating((float) 0);
        sellerDTO.setPhoneNumber(sellerRequestDTO.getPhoneNumber());
        sellerDTO.setProvince(sellerRequestDTO.getProvince());
        sellerDTO.setDistrict(sellerRequestDTO.getDistrict());
        sellerDTO.setCommune(sellerRequestDTO.getCommune());
        sellerDTO.setAddress(sellerRequestDTO.getAddress());
        sellerDTO.setPaymentMethods(paymentMethodDTOs);
        sellerDTO.setUpdatedAt(Instant.now());

        return toSellerDTO(sellerRepository.save(toSellerEntity(sellerDTO)));
    }

    private Set<PaymentMethodDTO> findPaymentMethodsByName(final Set<String> paymentMethodDTOS) {
        return paymentMethodDTOS.stream()
                .map(paymentMethodService::findByName)
                .collect(Collectors.toSet());
    }

    private void verifyIfEmailSellerAvailable(final String emailSeller) {
        final Optional<SellerDTO> sellerDTO = sellerRepository.findByEmailSeller(emailSeller)
                .map(SellerDTOMapper::toSellerDTO);

        if (sellerDTO.isPresent()) {
            throw supplyEmailSellerUsedError(emailSeller).get();
        }
    }

    private void verifyPermissionSellerRegister(final UserDTO userDTO) {
        for (RoleDTO role : userDTO.getRoles()) {
            if (role.getName().equals("ROLE_SELLER")) {
                throw supplyErrorProcesses("You can't register as seller again. You are already registered as a seller").get();
            }
        }
    }

    private void sendEmailFeedbackUser(final SellerDTO sellerDTO, final String linkRegister, final String contentFeedback) {
        try {
            final String subject = "[Grid Shop] - Update Your Seller Information";
            final String content = "<body style=\"padding: 0;margin: 0;\">\n" +
                    "  <div style=\"width: 600px;display: flex;justify-content: center;margin: auto;flex-direction: column;\">\n" +
                    "    <div style=\"padding: 20px;border: 1px solid #dadada;\">\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">Greetings, <strong>\"" + sellerDTO.getUser().getUsername() + "\"</strong>,</p>\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">\n" +
                    "        Thank you for your interest in becoming a seller on our platform. We appreciate your request and would like to inform you that we have received your application.\n" +
                    "       <br> Our team is currently reviewing your application and verifying the information provided.\n" +
                    "      </p>\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">\n" +
                    "        We would like to inform you that you need to adjust the following information to comply with our system's standards:\n" +
                    "        <br><strong>\"" + contentFeedback + "\"</strong>\n" +
                    "        <br>Please click the button below to update your information.\n" +
                    "      </p>\n" +
                    "      <a href=\"" + linkRegister + "\" style=\"padding: 8px;display: inline-block;cursor: pointer; border-radius: 3px;font-size: 15px;text-decoration: none;background-color: #0167f3;color: #fff;font-weight: 500;\">\n" +
                    "        Update Information\n" +
                    "      </a>\n" +
                    "      <br><br>\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">Thank you for using our service.</p>\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">If you have any questions, please don't hesitate to contact us immediately at <a href=\"mailto:gridshopvn@gmail.com\">gridshopvn@gmail.com</a>.</p>\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">Thank you,</p>\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">The Grid Shop Team</p>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</body>\n";

            formEmail(javaMailSender, sellerDTO.getEmailSeller(), subject, content);
        } catch (Exception ex) {
            throw supplyErrorProcesses("An error occurred while processing your request. Please try again later!").get();
        }
    }
}
