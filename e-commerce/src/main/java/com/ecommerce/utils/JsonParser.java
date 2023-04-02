package com.ecommerce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(final Class<T> targetClass, final String filename) throws IOException {
        final ClassLoader classLoader = targetClass.getClassLoader();
        final InputStream file = classLoader.getResourceAsStream(filename);

        return objectMapper.readValue(file, targetClass);
    }
}
