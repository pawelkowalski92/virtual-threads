package com.github.pawelkowalski92.gatto.guidance.infrastructure.configuration;

import com.github.pawelkowalski92.gatto.guidance.application.services.GattoGuidanceService;
import com.github.pawelkowalski92.gatto.guidance.application.core.wisdom.GattoWisdomPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GattoGuidanceServiceConfiguration {

    @Bean
    public GattoGuidanceService gattoGuidanceService(GattoWisdomPublisher gattoWisdomPublisher) {
        return new GattoGuidanceService(gattoWisdomPublisher);
    }

}
