package com.ecommerce.configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class PaypalConfiguration {

    @Value("${paypal.client.app}")
    private String clientId;

    @Value("${paypal.client.secret}")
    public String clientSecret;

    @Value("${paypal.mode}")
    public String mode;

    @Bean
    public Map<String, String> paypalConfig() {
        final Map<String, String> paypalConfig = new HashMap<>();
        paypalConfig.put("mode", mode);
        return paypalConfig;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        final APIContext apiContext = new APIContext(oAuthTokenCredential().getAccessToken());
        apiContext.setConfigurationMap(paypalConfig());
        
        return apiContext;
    }
}
