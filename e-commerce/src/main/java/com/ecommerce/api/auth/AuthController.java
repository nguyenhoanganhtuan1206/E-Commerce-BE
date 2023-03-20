package com.ecommerce.api.auth;

import com.ecommerce.api.auth.dto.UserSignUpRequestDTO;
import com.ecommerce.api.auth.dto.UserSignUpResponseDTO;
import com.ecommerce.domain.auth.JwtTokenService;
import com.ecommerce.domain.auth.JwtUserDetails;
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

import static com.ecommerce.api.auth.mapper.UserAuthMapper.toAuthentication;
import static com.ecommerce.error.CommonError.supplyValidationError;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public JwtTokenResponseDTO login(
            final @RequestBody UserLoginRequestDTO userLoginRequestDTO
    ) {
        /**
         * @ when `getPrincipal, it will return the `User` in `JwtUserDetails` I extend include
         * username, password, role and so on...
         * */

        final Authentication authentication = authenticationManager.authenticate(toAuthentication(userLoginRequestDTO));

        return JwtTokenResponseDTO.builder()
                .token(jwtTokenService.generateToken((JwtUserDetails) authentication.getPrincipal()))
                .build();
    }

    @PostMapping("/sign-up")
    public UserSignUpResponseDTO signUp(
            final @Valid @RequestBody UserSignUpRequestDTO userSignUpDTO,
            final BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw supplyValidationError(bindingResult.getFieldError().getDefaultMessage()).get();
        }

        return userService.signUp(userSignUpDTO);
    }
}
