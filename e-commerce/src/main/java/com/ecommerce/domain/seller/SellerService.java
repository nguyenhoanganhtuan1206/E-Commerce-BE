package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerSignUpRequestDTO;
import com.ecommerce.domain.address.AddressService;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.payment.PaymentMethodService;
import com.ecommerce.domain.payment.dto.PaymentMethodDTO;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.seller.mapper.SellerDTOMapper;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.seller.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecommerce.domain.seller.SellerError.supplyEmailSellerUsedError;
import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerEntity;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final AddressService addressService;

    private final PaymentMethodService paymentMethodService;

    private final AuthsProvider authsProvider;

    public SellerDTO findById(final UUID sellerId) {
        return toSellerDTO(sellerRepository.findById(sellerId)
                .orElseThrow(supplySellerNotFound(sellerId)));
    }

    public SellerDTO getRegisteredSellerDetailsByUserId() {
        return toSellerDTO(sellerRepository.findByUserId(authsProvider.getCurrentUserId())
                .orElse(null));
    }

    public void registerNewSeller(final SellerSignUpRequestDTO sellerRequestDTO) {
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        verifyIfEmailSellerAvailable(sellerRequestDTO.getEmailSeller());
        verifyPermissionSellerRegister(userDTO);

        final RoleDTO roleDTO = roleService.findByName("ROLE_SELLER");
        userDTO.getRoles().add(roleDTO);
        userService.save(userDTO);

        final Set<PaymentMethodDTO> paymentMethodDTOs = sellerRequestDTO.getNamePaymentMethods().stream()
                .map(paymentMethodService::findByName)
                .collect(Collectors.toSet());

        final SellerDTO sellerDTO = SellerDTO.builder()
                .sellerName(sellerRequestDTO.getSellerName())
                .emailSeller(sellerRequestDTO.getEmailSeller())
                .sellerRating((float) 0)
                .phoneNumber(sellerRequestDTO.getPhoneNumber())
                .city(getCityName(sellerRequestDTO.getCity()))
                .district(getDistrictName(sellerRequestDTO.getDistrict()))
                .commune(getCommuneName(sellerRequestDTO.getCommune()))
                .address(sellerRequestDTO.getAddress())
                .sellerApproval(false)
                .user(userDTO)
                .paymentMethodDTOs(paymentMethodDTOs)
                .build();

        sellerRepository.save(toSellerEntity(sellerDTO));
    }

    private String getCityName(String cityId) {
        return addressService.findProvinceById(cityId).getName();
    }

    private String getDistrictName(String districtId) {
        return addressService.findDistrictById(districtId).getName();
    }

    private String getCommuneName(String communeId) {
        return addressService.findCommuneById(communeId).getName();
    }

    private void verifyIfEmailSellerAvailable(final String emailSeller) {
        final Optional<SellerDTO> sellerDTO = sellerRepository.findByEmailSeller(emailSeller)
                .map(SellerDTOMapper::toSellerDTO);

        if (sellerDTO.isPresent()) {
            throw supplyEmailSellerUsedError("Email address registered by another person. Please try another email.").get();
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
