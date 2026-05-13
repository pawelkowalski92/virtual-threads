package com.github.pawelkowalski92.gatto.image.infrastructure.configuration;

import com.github.pawelkowalski92.gatto.image.application.core.guidance.GattoGuidanceReceiver;
import com.github.pawelkowalski92.gatto.image.infrastructure.adapters.spi.guidance.GattoGuidanceHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GattoGuidanceReceiverConfiguration {

    @Bean
    public RestClient gattoGuidanceClient(@Value("${gatto-meowelho.guidance.base-url}") String baseUrl) {
        return RestClient.create(baseUrl);
    }

    @Bean
    public GattoGuidanceReceiver gattoGuidance(RestClient gattoGuidanceClient) {
        return new GattoGuidanceHttpClient(gattoGuidanceClient);
    }

}
