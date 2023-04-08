package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.seller.mapper.SellerDTOMapper;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.seller.SellerRepository;
import com.ecommerce.persistent.status.Status;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecommerce.domain.seller.SellerError.supplyEmailSellerUsedError;
import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.*;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final PaymentMethodService paymentMethodService;

    private final AuthsProvider authsProvider;

    public List<SellerDTO> findAllSortedByCreatedAt() {
        return toSellerDTOs(sellerRepository.findAllSortedByCreatedAt());
    }

    public SellerDTO findById(final UUID sellerId) {
        return toSellerDTO(sellerRepository.findById(sellerId)
                .orElseThrow(supplySellerNotFound(sellerId)));
    }

    public SellerDTO findByUserId(final UUID userId) {
        return toSellerDTO(sellerRepository.findByUserId(userId).orElseThrow(supplySellerNotFound(userId)));
    }

    public Optional<SellerDTO> getRegisteredSellerDetailsByUserId() {
        return sellerRepository.findByUserId(authsProvider.getCurrentUserId())
                .map(SellerDTOMapper::toSellerDTO);
    }

    public void registerNewSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        verifyPermissionSellerRegister(userDTO);

        createNewSeller(sellerRequestDTO, userDTO);
    }

    public void updateSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final SellerDTO sellerDTO = findByUserId(authsProvider.getCurrentUserId());
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        if (!StringUtils.equals(sellerRequestDTO.getEmailSeller(), sellerDTO.getEmailSeller())) {
            verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        }
        verifyPermissionSellerRegister(userDTO);
        updateSeller(sellerDTO, sellerRequestDTO);
    }

    private void createNewSeller(final SellerSignUpRequestDTO sellerRequestDTO, final UserDTO userDTO) {
        final Set<PaymentMethodDTO> paymentMethodDTOs = sellerRequestDTO.getNamePaymentMethods().stream()
                .map(paymentMethodService::findByName)
                .collect(Collectors.toSet());

        final SellerDTO sellerDTO = SellerDTO.builder()
                .sellerName(sellerRequestDTO.getSellerName())
                .emailSeller(sellerRequestDTO.getEmailSeller())
                .sellerRating((float) 0)
                .phoneNumber(sellerRequestDTO.getPhoneNumber())
                .city(sellerRequestDTO.getCity())
                .district(sellerRequestDTO.getDistrict())
                .commune(sellerRequestDTO.getCommune())
                .address(sellerRequestDTO.getAddress())
                .sellerApproval(Status.PENDING)
                .user(userDTO)
                .createdAt(Instant.now())
                .paymentMethods(paymentMethodDTOs)
                .build();

        sellerRepository.save(toSellerEntity(sellerDTO));
    }

    private void updateSeller(final SellerDTO sellerDTO, final SellerSignUpRequestDTO sellerRequestDTO) {
        final Set<PaymentMethodDTO> paymentMethodDTOs = sellerRequestDTO.getNamePaymentMethods().stream()
                .map(paymentMethodService::findByName)
                .collect(Collectors.toSet());

        sellerDTO.setSellerName(sellerRequestDTO.getSellerName());
        sellerDTO.setEmailSeller(sellerRequestDTO.getEmailSeller());
        sellerDTO.setSellerRating((float) 0);
        sellerDTO.setPhoneNumber(sellerRequestDTO.getPhoneNumber());
        sellerDTO.setCity(sellerRequestDTO.getCity());
        sellerDTO.setDistrict(sellerRequestDTO.getDistrict());
        sellerDTO.setCommune(sellerRequestDTO.getCommune());
        sellerDTO.setAddress(sellerRequestDTO.getAddress());
        sellerDTO.setPaymentMethods(paymentMethodDTOs);

        sellerRepository.save(toSellerEntity(sellerDTO));
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
}
