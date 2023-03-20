package com.ecommerce.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public class TokenGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
