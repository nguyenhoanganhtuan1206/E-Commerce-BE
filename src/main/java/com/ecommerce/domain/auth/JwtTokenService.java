package com.ecommerce.domain.auth;

import com.ecommerce.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ecommerce.error.CommonError.supplyAccessDeniedException;
import static com.ecommerce.error.CommonError.supplyUnauthorizedException;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;


@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private static final Clock clock = DefaultClock.INSTANCE;

    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_USER_ID = "userId";

    private final JwtProperties jwtProperties;

    public Authentication parse(final String token) {
        try {
            if (isBlank(token)) {
                return null;
            }
            
            final Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();

            if (isBlank(claims.getSubject())) {
                return null;
            }

            if (claims.getExpiration().before(clock.now())) {
                throw supplyAccessDeniedException("Your token has expired. Please log in again.").get();
            }

            final String claimRoles = claims.get(CLAIM_ROLES, String.class);
            if (isBlank(claimRoles)) {
                return null;
            }

            return new UserAuthenticationToken(
                    UUID.fromString(claims.get(CLAIM_USER_ID).toString()),
                    claims.getSubject(),
                    Arrays.stream(split(claimRoles, ","))
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );
        } catch (Exception exception) {
            throw supplyUnauthorizedException("Authentication failed. Please ensure that you have provided the correct credentials.").get();
        }
    }

    public String generateToken(final JwtUserDetails userDetails) {
        final Date createdDate = clock.now();
        final Date expirationDate = new Date(createdDate.getTime() + jwtProperties.getExpiration() * 1000);

        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .claim(CLAIM_ROLES, String.join(",", roles))
                .claim(CLAIM_USER_ID, userDetails.getUserId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }
}
