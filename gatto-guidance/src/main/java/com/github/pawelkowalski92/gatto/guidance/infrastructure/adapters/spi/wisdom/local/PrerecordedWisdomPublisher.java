package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.local;

import com.github.pawelkowalski92.gatto.guidance.application.core.wisdom.GattoWisdomPublisher;
import com.github.pawelkowalski92.gatto.guidance.application.core.model.GattoWisdom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class PrerecordedWisdomPublisher implements GattoWisdomPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PrerecordedWisdomProperties prerecordedWisdomProperties;
    private final RandomizedSelector selector;
    private final Duration publishingDelay;

    public PrerecordedWisdomPublisher(PrerecordedWisdomProperties prerecordedWisdomProperties,
                                      RandomizedSelector selector,
                                      Duration publishingDelay) {
        this.prerecordedWisdomProperties = prerecordedWisdomProperties;
        this.selector = selector;
        this.publishingDelay = publishingDelay;
    }

    @Override
    public GattoWisdom wisdomForMood(GattoWisdom.Mood mood) {
        LOGGER.info("Recalling random wisdom for specific mood: {}", mood);
        return publishWithDelay(() -> findPrerecordedWisdom(mood));
    }

    @Override
    public GattoWisdom randomWisdom() {
        LOGGER.info("Recalling random wisdom");
        var randomMood = selector.selectRandomFrom(GattoWisdom.Mood.class);
        return publishWithDelay(() -> findPrerecordedWisdom(randomMood));
    }

    private GattoWisdom findPrerecordedWisdom(GattoWisdom.Mood mood) {
        var sentences = prerecordedWisdomProperties.findForMood(mood);
        var sentence = selector.selectRandomFrom(sentences);
        return new GattoWisdom(mood, sentence);
    }

    private <T> T publishWithDelay(Supplier<? extends T> publisher) {
        try {
            TimeUnit.MILLISECONDS.sleep(publishingDelay.toMillis());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Thread: {} was interrupted (app shutdown?)", Thread.currentThread(), ex);
        }
        return publisher.get();
    }

}
