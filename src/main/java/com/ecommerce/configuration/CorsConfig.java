package com.ecommerce.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${api.allow-hosts}")
    private String allowedHosts;

    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(asList(allowedHosts.split(",")));
        config.setAllowedMethods(asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(singletonList("*"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}