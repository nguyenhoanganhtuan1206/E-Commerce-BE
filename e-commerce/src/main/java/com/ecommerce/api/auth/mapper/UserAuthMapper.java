package com.ecommerce.api.auth.mapper;

import com.ecommerce.api.auth.UserLoginRequestDTO;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@UtilityClass
public class UserAuthMapper {

    public static Authentication toAuthentication(final UserLoginRequestDTO userLoginRequestDTO) {
        return new UsernamePasswordAuthenticationToken(
                userLoginRequestDTO.getEmail(),
                userLoginRequestDTO.getPassword()
        );
    }
}
