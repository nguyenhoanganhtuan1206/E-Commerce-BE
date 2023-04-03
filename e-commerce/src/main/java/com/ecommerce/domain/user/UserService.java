package com.ecommerce.domain.user;

import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.profile.dto.UserUpdatePasswordDTO;
import com.ecommerce.api.profile.dto.UserUpdateRequestDTO;
import com.ecommerce.api.profile.dto.UserUpdateResponseDTO;
import com.ecommerce.api.user.dto.UserRequestResetPasswordDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.domain.location.LocationService;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.user.mapper.UserDTOMapper;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.ecommerce.domain.location.LocationError.supplyAddressAvailable;
import static com.ecommerce.domain.location.mapper.LocationDTOMapper.toLocationDTO;
import static com.ecommerce.domain.user.UserError.*;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTOs;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserEntity;
import static com.ecommerce.domain.user.mapper.UserSignUpMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserSignUpMapper.toUserSignUpResponseDTO;
import static com.ecommerce.domain.user.mapper.UserUpdateMapper.toUserUpdateResponseDTO;
import static com.ecommerce.error.CommonError.supplyErrorProcesses;
import static com.ecommerce.error.CommonError.supplyValidationError;
import static com.ecommerce.utils.FormEmailSender.formEmail;
import static com.ecommerce.utils.TokenGenerator.generateToken;
import static com.ecommerce.utils.TokenGenerator.parse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final LocationService locationService;

    private final AuthsProvider authsProvider;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final String BASE_URL_FORGET_PASSWORD = "http://localhost:3000/reset-password/";

    private final JavaMailSender javaMailSender;

    public List<UserDTO> findAll() {
        return toUserDTOs(userRepository.findAll());
    }

    public UserDTO save(final UserDTO userDTO) {
        return toUserDTO(userRepository.save(toUserEntity(userDTO)));
    }

    public UserSignUpResponseDTO signUp(final UserSignUpRequestDTO userRequestDTO) {
        verifyIfUserAvailable(userRequestDTO.getEmail());

        final UserDTO userDTO = toUserDTO(userRequestDTO);
        final RoleDTO roleDTO = roleService.findByName("ROLE_USER");

        userDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userDTO.setCreatedAt(Instant.now());
        userDTO.setRoles(Collections.singleton(roleDTO));
        userDTO.setAddress(userRequestDTO.getAddress());

        return toUserSignUpResponseDTO(userRepository.save(toUserEntity(userDTO)));
    }

    public UserDTO findByEmail(final String email) {
        return toUserDTO(userRepository.findByEmail(email).orElseThrow(supplyUserNotFound(email)));
    }

    public UserDTO findById(final UUID userId) {
        return toUserDTO(userRepository.findById(userId).orElseThrow(supplyUserNotFound(userId)));
    }

    public UserUpdateResponseDTO updatePassword(final UserUpdatePasswordDTO userRequestDTO) {
        final UserDTO user = findById(authsProvider.getCurrentUserId());

        if (!passwordEncoder.matches(userRequestDTO.getCurrentPassword(), user.getPassword())) {
            throw supplyValidationError("The password you entered does not match your current password.").get();
        }

        if (passwordEncoder.matches(userRequestDTO.getNewPassword(), user.getPassword())) {
            throw supplyConflictError("You used this password recently. Please choose a different one.").get();
        }

        user.setPassword(passwordEncoder.encode(userRequestDTO.getNewPassword()));

        return toUserUpdateResponseDTO(userRepository.save(toUserEntity(user)));
    }

    public UserUpdateResponseDTO update(final UserUpdateRequestDTO userUpdate) {
        final UserDTO user = findById(authsProvider.getCurrentUserId());

        if (!user.getEmail().equals(userUpdate.getEmail())) {
            verifyIfUserAvailable(userUpdate.getEmail());

            user.setEmail(userUpdate.getEmail());
        }

        user.setPhoneNumber(userUpdate.getPhoneNumber());
        user.setUsername(userUpdate.getUsername());
        user.setAddress(userUpdate.getAddress());
        user.setUpdatedAt(Instant.now());

        return toUserUpdateResponseDTO(userRepository.save(toUserEntity(user)));
    }

    public LocationDTO addLocation(final LocationRequestDTO locationRequestDTO) {
        final LocationDTO location = toLocationDTO(locationRequestDTO);
        final UserDTO userDTO = findById(authsProvider.getCurrentUserId());

        verifyIfAddressAvailable(userDTO.getLocations(), locationRequestDTO.getAddress());

        location.setUser(userDTO);

        return locationService.save(location);
    }

    /* FORGET PASSWORD HANDLER */
    public void handleForgetPassword(final String email) {
        final UserDTO userDTO = findByEmail(email);

        /* Generate token */
        final String token = generateToken(userDTO.getEmail(), (long) 1000000);

        final String linkResetPassword = BASE_URL_FORGET_PASSWORD + token;
        sendEmailResetPassword(userDTO, linkResetPassword);
    }

    public void handleResetPassword(final UserRequestResetPasswordDTO userRequestDTO, final String token) {
        final Authentication authentication = parse(token);
        final UserDTO user = findByEmail(authentication.getCredentials().toString());

        if (passwordEncoder.matches(userRequestDTO.getNewPassword(), user.getPassword())) {
            throw supplyConflictError("You used this password recently. Please choose a different one.").get();
        }

        user.setPassword(passwordEncoder.encode(userRequestDTO.getNewPassword()));
        userRepository.save(toUserEntity(user));
    }

    private void sendEmailResetPassword(final UserDTO user, final String linkResetPassword) {
        try {
            final String subject = "Here's the link to reset your password";
            String content = "<body style=\"padding: 0;margin: 0;\">" +
                    "    <div style=\"width: 600px;" +
                    "    display: flex;" +
                    "    justify-content: center;" +
                    "    margin: auto;" +
                    "    flex-direction: column;\">" +
                    "        <div style=\"padding: 20px;border: 1px solid #dadada;\">" +
                    "            <p style=\"font-size: 16px;color: #000;\">Greetings, \"" + user.getUsername() + "\"</p>" +
                    "            <p style=\"font-size: 16px;color: #000;\">We received a request to reset your password.<br>Click the button" +
                    "                below to setup a new password</p>" +
                    "            <a href=\"" + linkResetPassword + "\" style=\"padding: 8px;display: inline-block;cursor: pointer; border-radius: 3px;" +
                    "            font-size: 15px;text-decoration: none;background-color: #0167f3;color: #fff;font-weight: 500;\">Change" +
                    "                my password</a>" +
                    "            <p>This link will expire after 10 minutes. If you didn't request a password reset , ignore this email and continue using your current password." +
                    "            <br>" +
                    "            Thank you for using our service.<br>" +
                    "            <br>If you have any question, Please contact us immediately at <a href=\"mailto:gridshopvn@gmail.com@gmail.com\">gridshopvn@gmail.com</a>" +
                    "            </p>" +
                    "            <p>Thanks you.</p>" +
                    "            <p>Grid Shop Team.</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>";

            formEmail(javaMailSender, user.getEmail(), subject, content);
        } catch (Exception ex) {
            throw supplyErrorProcesses("An error occurred while processing your request. Please try again later!").get();
        }
    }
    /* FORGET PASSWORD HANDLER */

    private void verifyIfAddressAvailable(final Set<LocationDTO> locationDTOS, final String addressUpdate) {
        for (LocationDTO location : locationDTOS) {
            if (location.getAddress().equals(addressUpdate)) {
                throw supplyAddressAvailable(addressUpdate).get();
            }
        }
    }

    private void verifyIfUserAvailable(final String email) {
        Optional<UserDTO> user = userRepository.findByEmail(email).map(UserDTOMapper::toUserDTO);

        if (user.isPresent()) {
            throw supplyUserExisted(email).get();
        }
    }
}
