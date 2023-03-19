package com.ecommerce.domain.user;

import com.ecommerce.api.auth.dto.UserAuthRequestDTO;
import com.ecommerce.api.auth.dto.UserAuthResponseDTO;
import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.api.user.dto.UserUpdateRequestDTO;
import com.ecommerce.api.user.dto.UserUpdateResponseDTO;
import com.ecommerce.domain.location.LocationService;
import com.ecommerce.domain.user.mapper.UserDTOMapper;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.location.LocationError.supplyAddressAvailable;
import static com.ecommerce.domain.user.UserError.supplyUserExisted;
import static com.ecommerce.domain.user.UserError.supplyUserNotFound;
import static com.ecommerce.domain.user.mapper.UserAuthMapper.toUserEntity;
import static com.ecommerce.domain.user.mapper.UserAuthMapper.toUserResponseDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTOs;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserEntity;
import static com.ecommerce.domain.user.mapper.UserUpdateMapper.toUserUpdateDTO;
import static com.ecommerce.error.CommonError.supplyValidationError;
import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final LocationService locationService;

    public List<UserDTO> findAll() {
        return toUserDTOs(userRepository.findAll());
    }

    public UserAuthResponseDTO signUp(final UserAuthRequestDTO userRequestDTO) {
        verifyIfUserAvailable(userRequestDTO.getEmail());

        userRequestDTO.setCreatedAt(Instant.now());

        return toUserResponseDTO(userRepository.save(toUserEntity(userRequestDTO)));
    }

    public UserDTO findById(final UUID userId) {
        return toUserDTO(userRepository.findById(userId).orElseThrow(supplyUserNotFound(userId)));
    }

    public UserDTO findByEmail(final String email) {
        return toUserDTO(userRepository.findByEmail(email).orElseThrow(supplyUserNotFound(email)));
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

    public LocationDTO addLocation(final UUID userId, final LocationDTO locationDTO) {
        final UserDTO userDTO = findById(userId);

        verifyIfAddressAvailable(userDTO.getLocations(), locationDTO.getAddress());

        locationDTO.setUserDTO(userDTO);

        return locationService.save(locationDTO);
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
