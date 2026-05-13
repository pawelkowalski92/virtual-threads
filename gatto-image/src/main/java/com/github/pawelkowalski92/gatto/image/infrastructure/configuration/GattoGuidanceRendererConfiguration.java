package com.github.pawelkowalski92.gatto.image.infrastructure.configuration;

import com.github.pawelkowalski92.gatto.image.application.services.renderer.CPUPoolDelegatingRenderer;
import com.github.pawelkowalski92.gatto.image.application.services.renderer.GattoGuidanceRenderer;
import com.github.pawelkowalski92.gatto.image.application.services.renderer.SingleThreadRenderer;
import com.github.pawelkowalski92.gatto.image.application.core.guidance.GattoGuidanceReceiver;
import com.github.pawelkowalski92.gatto.image.application.services.generator.GattoImageGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;

@Configuration
public class GattoGuidanceRendererConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public GattoImageGeneratorFactory gattoImageGeneratorFactory() {
        return new GattoImageGeneratorFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "gatto-meowelho.renderer.use-cpu-pool", havingValue = "false", matchIfMissing = true)
    public GattoGuidanceRenderer singleThreadRenderer(
            GattoGuidanceReceiver gattoGuidance,
            GattoImageGeneratorFactory gattoImageGeneratorFactory,
            @Value("${gatto-meowelho.renderer.signature}") String signature
    ) {
        LOGGER.info("Using single worker thread renderer");
        return new SingleThreadRenderer(gattoGuidance, gattoImageGeneratorFactory, signature);
    }

    @Bean
    @ConditionalOnProperty(value = "gatto-meowelho.renderer.use-cpu-pool", havingValue = "true")
    public GattoGuidanceRenderer cpuPoolDelegatingRenderer(
            GattoGuidanceReceiver gattoGuidance,
            GattoImageGeneratorFactory gattoImageGeneratorFactory,
            @Value("${gatto-meowelho.renderer.signature}") String signature
    ) {
        LOGGER.info("Using CPU pool delegating renderer");
        return new CPUPoolDelegatingRenderer(gattoGuidance, gattoImageGeneratorFactory, signature);
    }

}
