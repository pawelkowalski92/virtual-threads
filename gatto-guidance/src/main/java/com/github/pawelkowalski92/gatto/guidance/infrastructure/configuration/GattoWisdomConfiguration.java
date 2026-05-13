package com.github.pawelkowalski92.gatto.guidance.infrastructure.configuration;

import com.github.pawelkowalski92.gatto.guidance.application.core.wisdom.GattoWisdomPublisher;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.ai.PersonifiedGattoMeowelhoPublisher;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.composite.WeightedGuidancePublisher;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.local.PrerecordedWisdomProperties;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.local.PrerecordedWisdomPublisher;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.local.RandomizedSelector;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.configuration.utils.YamlPropertySourceFactory;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Configuration
public class GattoWisdomConfiguration {

    @Bean
    @Primary
    public GattoWisdomPublisher compositePublisher(
            GattoWisdomPublisher prerecordedWisdomPublisher,
            GattoWisdomPublisher personifiedGattoMeowelhoPublisher,
            @Value("${gatto-meowelho.personified-response-percent:0}") double personifiedResponsePercent
    ) {
        return new WeightedGuidancePublisher(
                prerecordedWisdomPublisher,
                personifiedGattoMeowelhoPublisher,
                personifiedResponsePercent
        );
    }

    @Configuration
    public static class PersonifiedConfiguration {

        @Bean
        @ConditionalOnProperty(value = "gatto-meowelho.log-communication", havingValue = "true")
        public Advisor communicationLoggingAdvisor() {
            return SimpleLoggerAdvisor.builder()
                    .responseToString(response -> response.getResults().stream()
                            .map(Generation::getOutput)
                            .map(AbstractMessage::getText)
                            .collect(Collectors.joining(System.lineSeparator())))
                    .build();
        }

        @Bean
        public ChatClient gattoMeowelhoPersona(
                ChatClient.Builder modelApiBuilder,
                @Value("classpath:/gatto-meowelho/persona.txt") Resource gattoPersona,
                ObjectProvider<Advisor> advisorsProvider
        ) {
            return modelApiBuilder.defaultSystem(gattoPersona)
                    .defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                    .defaultAdvisors(advisorsProvider.orderedStream().toList())
                    .build();
        }

        @Bean
        public GattoWisdomPublisher personifiedGattoMeowelhoPublisher(
                ChatClient gattoMeowelhoPersona,
                @Value("classpath:/gatto-meowelho/wisdom-per-mood.txt") Resource wisdomForMoodSource,
                @Value("classpath:/gatto-meowelho/random-wisdom.txt") Resource randomWisdomSource
        ) {
            return new PersonifiedGattoMeowelhoPublisher(gattoMeowelhoPersona, wisdomForMoodSource, randomWisdomSource);
        }

    }

    @Configuration
    @EnableConfigurationProperties(PrerecordedWisdomProperties.class)
    @PropertySource(value = "classpath:/gatto-meowelho/prerecorded-sentences.yaml", factory = YamlPropertySourceFactory.class)
    public static class PrerecordedConfiguration {

        @Bean
        public RandomizedSelector randomizedSelector() {
            return new RandomizedSelector(ThreadLocalRandom.current());
        }

        @Bean
        public GattoWisdomPublisher prerecordedWisdomPublisher(
                PrerecordedWisdomProperties properties,
                RandomizedSelector randomizedSelector,
                @Value("${gatto-meowelho.prerecorded-publishing-delay}") Duration publishingDelay
        ) {
            return new PrerecordedWisdomPublisher(properties, randomizedSelector, publishingDelay);
        }

    }

}
