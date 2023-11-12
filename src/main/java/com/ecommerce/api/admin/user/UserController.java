package com.ecommerce.api.admin.user;

import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.domain.user.mapper.UserDTOMapper.toUserDTO;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("{userId}")
    public UserDTO findById(final @PathVariable UUID userId) {
        return toUserDTO(userService.findById(userId));
    }
}
