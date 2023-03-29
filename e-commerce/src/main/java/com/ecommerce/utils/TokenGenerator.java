package com.ecommerce.utils;

import com.ecommerce.domain.auth.UserAuthenticationToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static com.ecommerce.error.CommonError.supplyAccessDeniedException;
import static com.ecommerce.error.CommonError.supplyUnauthorizedException;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class TokenGenerator {

    private static final String SECRET_KEY = "secret_key";
    private static final Clock clock = DefaultClock.INSTANCE;

    public static String generateToken(
            final String subject,
            final Long timeExpire
    ) {
        final JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);
        if (timeExpire != null) {
            builder.setExpiration(new Date(System.currentTimeMillis() + timeExpire));
        }
        return builder.compact();
    }

    public static Authentication parse(final String token) {
        try {
            if (isBlank(token)) {
                throw supplyUnauthorizedException("This token is invalid. Please send another request with a valid token.").get();
            }

            final Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            if (isBlank(claims.getSubject())) {
                throw supplyUnauthorizedException("This token is invalid. Please send another request with a valid token.").get();
            }

            if (claims.getExpiration().before(clock.now())) {
                throw supplyAccessDeniedException("Your token has expired. Please send another request.").get();
            }

            return new UserAuthenticationToken(null, claims.getSubject(), null);
        } catch (Exception exception) {
            throw supplyUnauthorizedException("This token is invalid. Please send another request with a valid token.").get();
        }
    }
}
