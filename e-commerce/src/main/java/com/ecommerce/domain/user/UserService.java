package com.ecommerce.domain.user;

import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.api.location.dto.LocationRequestDTO;
import com.ecommerce.api.user.dto.UserUpdateRequestDTO;
import com.ecommerce.api.user.dto.UserUpdateResponseDTO;
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
import static com.ecommerce.domain.user.UserError.supplyUserExisted;
import static com.ecommerce.domain.user.UserError.supplyUserNotFound;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTOs;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserEntity;
import static com.ecommerce.domain.user.mapper.UserSignUpMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserSignUpMapper.toUserResponseDTO;
import static com.ecommerce.domain.user.mapper.UserUpdateMapper.toUserUpdateDTO;
import static com.ecommerce.error.CommonError.supplyValidationError;
import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final LocationService locationService;

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

        return toUserResponseDTO(userRepository.save(toUserEntity(userDTO)));
    }

    public UserDTO findById(final UUID userId) {
        return toUserDTO(userRepository.findById(userId)
                .orElseThrow(supplyUserNotFound(userId)));
    }

    public UserUpdateResponseDTO updateInfo(final UUID userId, final UserUpdateRequestDTO userUpdate) {
        final UserDTO user = findById(userId);

        if (!user.getEmail().equals(userUpdate.getEmail())) {
            verifyIfUserAvailable(userUpdate.getEmail());

            user.setEmail(userUpdate.getEmail());
        }

        if (isNotBlank(userUpdate.getPassword())) {
            validatePassword(userUpdate.getPassword());

            user.setPassword(userUpdate.getPassword());
        }

        user.setPhoneNumber(userUpdate.getPhoneNumber());
        user.setUsername(userUpdate.getUsername());
        user.setUpdatedAt(Instant.now());

        return toUserUpdateDTO(userRepository.save(toUserEntity(user)));
    }

    public LocationDTO addLocation(final UUID userId, final LocationRequestDTO locationRequestDTO) {
        final LocationDTO location = toLocationDTO(locationRequestDTO);
        final UserDTO userDTO = findById(userId);

        verifyIfAddressAvailable(userDTO.getLocations(), locationRequestDTO.getAddress());

        location.setUser(userDTO);

        return locationService.save(location);
    }

    private void validatePassword(final String password) {
        if (password.length() < 6 || password.length() > 30) {
            throw supplyValidationError("Password must be at between 6 to 30 characters").get();
        }
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
