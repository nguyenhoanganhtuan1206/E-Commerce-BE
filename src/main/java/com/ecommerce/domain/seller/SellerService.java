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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.seller.SellerError.supplyEmailSellerUsedError;
import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerResponseDTOMapper.toSellerResponseDTO;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final AuthsProvider authsProvider;

    private final String ROLE_SELLER = "ROLE_SELLER";

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

        // TODO: 6/13/2023 ADD SEND EMAIL FEEDBACK USER
    }

    public SellerEntity approveSellerRequest(final UUID sellerId) {
        final SellerEntity sellerFound = findById(sellerId);
        final UserEntity userFound = userService.findById(sellerFound.getUser().getId());

        userFound.getRoles().add(roleService.findByName(ROLE_SELLER));
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
}
