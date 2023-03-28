package com.ecommerce.domain.user;

import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.profile.dto.UserUpdatePasswordDTO;
import com.ecommerce.api.profile.dto.UserUpdateRequestDTO;
import com.ecommerce.api.profile.dto.UserUpdateResponseDTO;
import com.ecommerce.domain.auth.AuthsProvider;
import com.ecommerce.domain.location.LocationDTO;
import com.ecommerce.domain.location.LocationService;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.role.RoleService;
import com.ecommerce.domain.user.mapper.UserDTOMapper;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
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
import static com.ecommerce.error.CommonError.supplyValidationError;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final LocationService locationService;

    private final AuthsProvider authsProvider;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return toUserDTOs(userRepository.findAll());
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
