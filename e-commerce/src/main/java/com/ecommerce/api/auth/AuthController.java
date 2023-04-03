package com.ecommerce.api.auth;

import com.ecommerce.api.auth.dto.TokenRefreshRequest;
import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.domain.auth.JwtTokenService;
import com.ecommerce.domain.auth.JwtUserDetails;
import com.ecommerce.domain.role.RoleDTO;
import com.ecommerce.domain.user.UserDTO;
import com.ecommerce.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static com.ecommerce.api.auth.mapper.UserAuthMapper.toAuthentication;
import static com.ecommerce.error.CommonError.supplyUnauthorizedException;
import static com.ecommerce.error.CommonError.supplyValidationError;
import static com.ecommerce.error.ValidationErrorHandling.handleValidationError;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public JwtTokenResponseDTO login(final @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        /**
         * @ when `getPrincipal, it will return the `User` in `JwtUserDetails` I extend include
         * username, password, role and so on...
         * */
        try {
            final Authentication authentication = authenticationManager.authenticate(toAuthentication(userLoginRequestDTO));
            final UserDTO userDTO = userService.findByEmail(userLoginRequestDTO.getEmail());

            return generateJwtToken(userDTO, (JwtUserDetails) authentication.getPrincipal());
        } catch (Exception exception) {
            throw supplyUnauthorizedException("Bad credentials. Please check your inputs again!").get();
        }
    }

    @PostMapping("/refreshToken")
    public JwtTokenResponseDTO refreshToken(final @Valid @RequestBody TokenRefreshRequest request,
                                            final BindingResult bindingResult
    ) {
        try {
            handleValidationError(bindingResult);

            final Authentication authentication = jwtTokenService.parse(request.getToken());
            final UserDTO userDTO = userService.findByEmail(authentication.getPrincipal().toString());

            return generateJwtToken(userDTO, (JwtUserDetails) authentication.getPrincipal());
        } catch (Exception exception) {
            throw supplyUnauthorizedException("Your refresh token you provided is invalid!").get();
        }
    }

    @PostMapping("/sign-up")
    public UserSignUpResponseDTO signUp(final @Valid @RequestBody UserSignUpRequestDTO userSignUpDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        return userService.signUp(userSignUpDTO);
    }

    private JwtTokenResponseDTO generateJwtToken(final UserDTO userDTO, final JwtUserDetails userDetails) {
        return JwtTokenResponseDTO.builder()
                .userId(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .roles(userDTO.getRoles().stream()
                        .map(RoleDTO::getName)
                        .collect(Collectors.toSet()))
                .token(jwtTokenService.generateToken(userDetails))
                .build();
    }
}
