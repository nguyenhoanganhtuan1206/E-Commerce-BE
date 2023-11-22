package com.ecommerce.domain.user;

import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.api.profile.dto.UserUpdatePasswordDTO;
import com.ecommerce.api.profile.dto.UserUpdateRequestDTO;
import com.ecommerce.api.profile.dto.UserUpdateResponseDTO;
import com.ecommerce.api.user.dto.UserRequestResetPasswordDTO;
import com.ecommerce.api.user.dto.UserResponseDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.email.EmailService;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.user.mapper.UserDTOMapper;
import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.user.UserEntity;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.domain.user.UserError.*;
import static com.ecommerce.domain.user.UserValidation.validateUsername;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTOs;
import static com.ecommerce.domain.user.mapper.UserResponseDTOMapper.toUserResponseDTO;
import static com.ecommerce.domain.user.mapper.UserSignUpMapper.toUserSignUpResponseDTO;
import static com.ecommerce.domain.user.mapper.UserUpdateMapper.toUserUpdateResponseDTO;
import static com.ecommerce.error.CommonError.supplyValidationError;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AuthsProvider authsProvider;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public List<UserDTO> findAll() {
        return toUserDTOs(userRepository.findAll());
    }

    public UserEntity save(final UserEntity user) {
        return userRepository.save(user);
    }

    public UserSignUpResponseDTO signUp(final UserSignUpRequestDTO userRequestDTO) {
        verifyIfUserAvailable(userRequestDTO.getEmail());
        validateUsername(userRequestDTO.getUsername());

        final RoleEntity role = roleService.findByName("ROLE_USER");

        final UserEntity userCreate = UserEntity.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .createdAt(Instant.now())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .roles(Collections.singleton(role))
                .build();

        return toUserSignUpResponseDTO(userRepository.save(userCreate));
    }

    public UserEntity findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(supplyUserNotFound("email", email));
    }

    public UserResponseDTO findProfileById(final UUID userId) {
        return toUserResponseDTO(userRepository.findProfileById(userId)
                .orElseThrow(supplyUserNotFound("id", userId)));
    }

    public UserEntity findById(final UUID userId) {
        return userRepository.findById(userId).orElseThrow(supplyUserNotFound("id", userId));
    }

    public UserEntity findByCodeResetPassword(final String code) {
        return userRepository.findByCodeResetPassword(code)
                .orElseThrow(supplierCodeResetPasswordInvalid("Your request to reset password is invalid"));
    }

    public UserUpdateResponseDTO updatePassword(final UserUpdatePasswordDTO userRequestDTO) {
        final UserEntity userUpdate = findById(authsProvider.getCurrentUserId());

        if (!passwordEncoder.matches(userRequestDTO.getCurrentPassword(), userUpdate.getPassword())) {
            throw supplyValidationError("The password you entered does not match your current password.").get();
        }

        if (passwordEncoder.matches(userRequestDTO.getNewPassword(), userUpdate.getPassword())) {
            throw supplyConflictError("You used this password recently. Please choose a different one.").get();
        }

        userUpdate.setPassword(passwordEncoder.encode(userRequestDTO.getNewPassword()));

        return toUserUpdateResponseDTO(userRepository.save(userUpdate));
    }

    public UserUpdateResponseDTO update(final UserUpdateRequestDTO userUpdate) {
        final UserEntity user = findById(authsProvider.getCurrentUserId());

        if (!user.getEmail().equals(userUpdate.getEmail())) {
            verifyIfUserAvailable(userUpdate.getEmail());

            user.setEmail(userUpdate.getEmail());
        }

        user.setPhoneNumber(userUpdate.getPhoneNumber());
        user.setUsername(userUpdate.getUsername());
        user.setUpdatedAt(Instant.now());

        return toUserUpdateResponseDTO(userRepository.save(user));
    }

    public void sendEmailForgetPassword(final String email) {
        final String codeResetPassword = randomUUID().toString();
        final UserEntity user = findByEmail(email);
        final String RESET_PASSWORD_DOMAIN = "http://localhost:3000/reset-password/code=" + codeResetPassword;

        user.setCodeResetPassword(codeResetPassword);
        user.setLastSendResetPasswordAt(Instant.now());

        userRepository.save(user);

        emailService.sendEmailForgetPassword(user, RESET_PASSWORD_DOMAIN);
    }

    public void verifyCodeResetPassword(final String codeResetPassword) {
        final UserEntity user = findByCodeResetPassword(codeResetPassword);

        verifyExpirationTimeResetPassword(user.getLastSendResetPasswordAt());
    }

    public void resetPassword(final UserRequestResetPasswordDTO requestResetPassword) {
        final UserEntity user = findByCodeResetPassword(requestResetPassword.getCode());

        verifyExpirationTimeResetPassword(user.getLastSendResetPasswordAt());

        final String newPassword = passwordEncoder.encode(requestResetPassword.getNewPassword());

        user.setPassword(newPassword);
        user.setCodeResetPassword(null);
        user.setLastSendResetPasswordAt(null);

        userRepository.save(user);
    }

    private void verifyExpirationTimeResetPassword(final Instant lastSendResetPassword) {
        final Instant expirationTime = lastSendResetPassword.plus(10, ChronoUnit.MINUTES);

        if (Instant.now().isAfter(expirationTime)) {
            throw supplierCodeResetPasswordInvalid("Your request to reset password is expired! Please request another one").get();
        }
    }

    private void verifyIfUserAvailable(final String email) {
        Optional<UserDTO> user = userRepository.findByEmail(email).map(UserDTOMapper::toUserDTO);

        if (user.isPresent()) {
            throw supplyUserExisted(email).get();
        }
    }
}
