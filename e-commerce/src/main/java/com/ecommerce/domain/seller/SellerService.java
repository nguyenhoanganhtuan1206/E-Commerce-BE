package com.ecommerce.domain.seller;

import com.ecommerce.api.seller.dto.SellerCreateRequestDTO;
import com.ecommerce.domain.address.AddressService;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.seller.mapper.SellerDTOMapper;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import com.ecommerce.persistent.seller.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.seller.SellerError.supplySellerNotFound;
import static com.ecommerce.domain.seller.SellerError.supplySellerPermission;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerDTO;
import static com.ecommerce.domain.seller.mapper.SellerDTOMapper.toSellerEntity;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static com.ecommerce.utils.FormEmailSender.formEmail;
import static com.ecommerce.utils.TokenGenerator.generateToken;
import static com.ecommerce.utils.TokenGenerator.parse;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final UserService userService;

    private final RoleService roleService;

    private final AddressService addressService;

    private final JavaMailSender javaMailSender;

    private final AuthsProvider authsProvider;
    private final String URL_REGISTER = "http://localhost:3000/registration-seller/token=";

    public SellerDTO findById(final UUID sellerId) {
        return toSellerDTO(sellerRepository.findById(sellerId).orElseThrow(supplySellerNotFound(sellerId)));
    }

    public SellerDTO findByEmailSeller(final String email) {
        return toSellerDTO(sellerRepository.findByEmailSeller(email).orElseThrow(supplySellerNotFound(email)));
    }

    public void requestConfirmationEmailForSale(final String email) {
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        for (RoleDTO role : userDTO.getRoles()) {
            if (role.getName().equals("ROLE_SELLER")) {
                supplyErrorProcesses("You can't register as seller again. You are already registered as a seller");
            }
        }

        final SellerDTO sellerDTO = SellerDTO.builder().emailSeller(email).confirmationToken(generateToken(email, (long) 1000000)).user(userDTO).build();

        final Optional<SellerDTO> sellerExisted = sellerRepository.findByEmailSeller(email).map(SellerDTOMapper::toSellerDTO);

        if (sellerExisted.isEmpty()) {
            sellerRepository.save(toSellerEntity(sellerDTO));
        }

        String registrationLink = URL_REGISTER + sellerDTO.getConfirmationToken();
        sendRegistrationEmail(userDTO, email, registrationLink);
    }

    public void registerNewSeller(final String confirmationToken, final SellerCreateRequestDTO sellerRequestDTO) {
        final Authentication authentication = parse(confirmationToken);
        final SellerDTO sellerDTO = findByEmailSeller(authentication.getCredentials().toString());
        final UserDTO userDTO = userService.findById(authsProvider.getCurrentUserId());

        if (!StringUtils.equals(sellerDTO.getUser().getId().toString(), userDTO.getId().toString())) {
            throw supplySellerPermission().get();
        }

        sellerDTO.setSellerName(sellerRequestDTO.getSellerName());
        sellerDTO.setSellerRating((float) 0);
        sellerDTO.setPhoneNumber(sellerRequestDTO.getPhoneNumber());
        sellerDTO.setCity(getCityName(sellerRequestDTO.getCity()));
        sellerDTO.setDistrict(getDistrictName(sellerRequestDTO.getDistrict()));
        sellerDTO.setCommune(getCommuneName(sellerRequestDTO.getCommune()));
        sellerDTO.setAddress(sellerRequestDTO.getAddress());
        sellerDTO.setConfirmationToken(null);

        userDTO.getRoles().add(roleService.findByName("ROLE_SELLER"));
        userService.save(userDTO);
        sellerRepository.save(toSellerEntity(sellerDTO));
    }


    private void sendRegistrationEmail(final UserDTO userDTO, final String emailSeller, final String registrationLink) {
        String subject = "Confirm Your Registration as a Seller on Our E-commerce Platform";
        String content = "<body style='padding: 0;margin: 0;'>" + "    <div style='width: 600px;" + "    display: flex;" + "    justify-content: center;" + "    margin: auto;" + "    flex-direction: column;'>" + "        <div style='padding: 20px;border: 1px solid #dadada;'>" + "            <p style='font-size: 16px;color: #000;'>Dear, \"" + userDTO.getUsername() + "\"</p>" + "            <p style='font-size: 16px;color: #000;'>Confirm Your Registration as a Seller on Our E-commerce Platform.<br> <strong>Click the button" + "                below to redirect next step for register </strong>. <br />    <p>This link will expire after 10 minutes. If you did not request this registration, please ignore this email.</p>" + "            <a href='" + registrationLink + "' target=\"_self\" style=\"padding: 8px;border: none;display: block;cursor: pointer; border-radius: 3px;margin: 12px 0" + "            font-size: 15px;text-decoration: none;background-color: #0167f3;color: #fff;font-weight: 500;\">Form " + "                Registration</a>" + "            <p>Thank you for using our service.<br>" + "            <br>If you have any question, Please contact us immediately at <a href='mailto:gridshopvn@gmail.com@gmail.com' style='color: #0167f3;'>gridshopvn@gmail.com</a>" + "            </p>" + "            <p>Thanks you.</p>" + "            <p>Grid Shop Team.</p>" + "        </div>" + "    </div>" + "</body>";
        formEmail(javaMailSender, emailSeller, subject, content);
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
}
