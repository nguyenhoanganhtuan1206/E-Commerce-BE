package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerResponseDTO;
import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.seller.SellerEntity;
import com.ecommerce.persistent.seller.SellerRepository;
import com.ecommerce.persistent.status.Status;
import com.ecommerce.persistent.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.seller.SellerError.supplyEmailSellerUsedError;
import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTO;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static com.ecommerce.utils.FormEmailSender.formEmail;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final AuthsProvider authsProvider;

    private final JavaMailSender javaMailSender;

    public List<SellerEntity> findAllSortedByCreatedAt() {
        return sellerRepository.findAllSortedByCreatedAt();
    }

    public SellerEntity findById(final UUID sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(supplySellerNotFound("id", sellerId.toString()));
    }

    public SellerEntity findByUserId(final UUID userId) {
        return sellerRepository.findByUserId(userId)
                .orElseThrow(supplySellerNotFound("user id", userId.toString()));
    }

    public Optional<SellerEntity> getRegisteredSellerDetailsByUserId() {
        return sellerRepository.findByUserId(authsProvider.getCurrentUserId());
    }

    public SellerResponseDTO registerNewSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final UserEntity userFound = userService.findById(authsProvider.getCurrentUserId());

        verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        verifyPermissionSellerRegister(userFound);

        return toSellerResponseDTO(createNewSeller(sellerRequestDTO, userFound));
    }

    public SellerResponseDTO updateSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final SellerEntity sellerExisting = findByUserId(authsProvider.getCurrentUserId());
        final UserEntity userFound = userService.findById(authsProvider.getCurrentUserId());

        if (!StringUtils.equals(sellerRequestDTO.getEmailSeller(), sellerExisting.getEmailSeller())) {
            verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        }
        verifyPermissionSellerRegister(userFound);

        return toSellerResponseDTO(updateSeller(sellerExisting, sellerRequestDTO));
    }

    public void sendFeedbackToUser(final UUID sellerId, final String contentFeedback) {
        final String BASE_URL_REGISTER_FORM = "http://localhost:3000/post-ad";
        final SellerEntity sellerFound = findById(sellerId);

        sendEmailFeedbackUser(sellerFound, BASE_URL_REGISTER_FORM, contentFeedback);
    }

    public SellerEntity approveSellerRequest(final UUID sellerId) {
        final SellerEntity sellerFound = findById(sellerId);
        final UserEntity userFound = userService.findById(sellerFound.getUser().getId());

        userFound.getRoles().add(roleService.findByName("ROLE_SELLER"));
        sellerFound.setSellerApproval(Status.ACTIVE);

        userService.save(userFound);
        return sellerRepository.save(sellerFound);
    }

    private SellerEntity createNewSeller(final SellerSignUpRequestDTO sellerRequestDTO, final UserEntity user) {
        final SellerEntity sellerCreate = SellerEntity.builder()
                .sellerName(sellerRequestDTO.getSellerName())
                .emailSeller(sellerRequestDTO.getEmailSeller())
                .sellerRating((float) 0)
                .phoneNumber(sellerRequestDTO.getPhoneNumber())
                .province(sellerRequestDTO.getProvince())
                .district(sellerRequestDTO.getDistrict())
                .commune(sellerRequestDTO.getCommune())
                .address(sellerRequestDTO.getAddress())
                .sellerApproval(Status.PENDING)
                .user(user)
                .createdAt(Instant.now())
                .build();

        return sellerRepository.save(sellerCreate);
    }

    private SellerEntity updateSeller(final SellerEntity sellerEntity, final SellerSignUpRequestDTO sellerRequestDTO) {
        sellerEntity.setSellerName(sellerRequestDTO.getSellerName());
        sellerEntity.setEmailSeller(sellerRequestDTO.getEmailSeller());
        sellerEntity.setSellerRating((float) 0);
        sellerEntity.setPhoneNumber(sellerRequestDTO.getPhoneNumber());
        sellerEntity.setProvince(sellerRequestDTO.getProvince());
        sellerEntity.setDistrict(sellerRequestDTO.getDistrict());
        sellerEntity.setCommune(sellerRequestDTO.getCommune());
        sellerEntity.setAddress(sellerRequestDTO.getAddress());
        sellerEntity.setUpdatedAt(Instant.now());

        return sellerRepository.save(sellerEntity);
    }


    private void verifyIfEmailSellerAvailable(final String emailSeller) {
        final Optional<SellerEntity> sellerExisting = sellerRepository.findByEmailSeller(emailSeller);

        if (sellerExisting.isPresent()) {
            throw supplyEmailSellerUsedError(emailSeller).get();
        }
    }

    private void verifyPermissionSellerRegister(final UserEntity user) {
        for (RoleEntity role : user.getRoles()) {
            if (role.getName().equals("ROLE_SELLER")) {
                throw supplyErrorProcesses("You can't register as seller again. You are already registered as a seller").get();
            }
        }
    }

    private void sendEmailFeedbackUser(final SellerEntity seller, final String linkRegister, final String contentFeedback) {
        try {
            final String subject = "[Grid Shop] - Update Your Seller Information";
            final String content = "<body style=\"padding: 0;margin: 0;\">\n" +
                    "  <div style=\"width: 600px;display: flex;justify-content: center;margin: auto;flex-direction: column;\">\n" +
                    "    <div style=\"padding: 20px;border: 1px solid #dadada;\">\n" +
                    "      <p style=\"font-size: 16px;color: #000;\">Greetings, <strong>\"" + seller.getUser().getUsername() + "\"</strong>,</p>\n" +
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

            formEmail(javaMailSender, seller.getEmailSeller(), subject, content);
        } catch (Exception ex) {
            throw supplyErrorProcesses("An error occurred while processing your request. Please try again later!").get();
        }
    }
}
