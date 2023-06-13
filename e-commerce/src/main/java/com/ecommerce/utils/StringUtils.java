package com.ecommerce.utils;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@UtilityClass
public class StringUtils {

    public static String replaceText(final String raw, final Map<String, String> replacements) {
        String content = raw;

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            content = content.replace("{{" + key + "}}", value);
        }

        return content;
    }

    public static String findCaptureGroup(final String text, final String regex) {
        final Pattern pattern = compile(regex);
        final Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new RuntimeException("No matched group can be found");
    }
}
