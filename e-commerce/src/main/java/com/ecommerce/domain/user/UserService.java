package com.ecommerce.domain.user;

import com.ecommerce.api.auth.dto.UserRequestDTO;
import com.ecommerce.api.auth.dto.UserResponseDTO;
import com.ecommerce.api.location.dto.LocationDTO;
import com.ecommerce.api.user.dto.UserDTO;
import com.ecommerce.api.user.dto.UserRequestUpdateDTO;
import com.ecommerce.domain.location.LocationService;
import com.ecommerce.domain.user.mapper.UserDTOMapper;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ecommerce.domain.location.LocationError.supplyAddressAvailable;
import static com.ecommerce.domain.user.UserError.supplyUserExisted;
import static com.ecommerce.domain.user.UserError.supplyUserNotFound;
import static com.ecommerce.domain.user.mapper.UserAuthMapper.*;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserEntity;
import static com.ecommerce.domain.user.mapper.UserDTOMapper.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final LocationService locationService;

    public List<UserDTO> findAll() {
        return toUserDTOs(userRepository.findAll());
    }

    public UserResponseDTO signUp(final UserRequestDTO userRequestDTO) {
        verifyIfUserAvailable(userRequestDTO.getEmail());

        return toUserResponseDTOWithoutLocations(userRepository.save(toUserEntityWithOutLocation(userRequestDTO)));
    }

    public UserDTO findById(final UUID userId) {
        return toUserDTO(userRepository.findById(userId).orElseThrow(supplyUserNotFound(userId)));
    }

    public UserResponseDTO updateInfo(final UUID userId, final UserRequestUpdateDTO userUpdate) {
        final UserDTO user = findById(userId);

        if (!user.getEmail().equals(userUpdate.getEmail())) {
            verifyIfUserAvailable(userUpdate.getEmail());

            user.setEmail(userUpdate.getEmail());
        }

        user.setPhoneNumber(userUpdate.getPhoneNumber());
        user.setUsername(userUpdate.getUsername());

        return toUserResponseDTO(userRepository.save(toUserEntity(user)));
    }

    public LocationDTO addLocation(final UUID userId, final LocationDTO locationDTO) {
        final UserDTO userDTO = findById(userId);

        verifyIfAddressAvailable(userDTO.getLocations(), locationDTO.getAddress());

        locationDTO.setUserDTO(userDTO);

        return locationService.save(locationDTO);
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
