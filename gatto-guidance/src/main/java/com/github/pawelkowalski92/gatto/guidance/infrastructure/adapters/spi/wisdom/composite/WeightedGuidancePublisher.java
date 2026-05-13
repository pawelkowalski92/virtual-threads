package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.composite;

import com.github.pawelkowalski92.gatto.guidance.application.core.wisdom.GattoWisdomPublisher;
import com.github.pawelkowalski92.gatto.guidance.application.core.model.GattoWisdom;

import java.util.concurrent.atomic.AtomicInteger;

public class WeightedGuidancePublisher implements GattoWisdomPublisher {

    private final GattoWisdomPublisher primaryPublisher;
    private final GattoWisdomPublisher secondaryPublisher;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final int distribution;

    public WeightedGuidancePublisher(GattoWisdomPublisher primaryPublisher,
                                     GattoWisdomPublisher secondaryPublisher,
                                     double secondaryWeight) {
        this.primaryPublisher = primaryPublisher;
        this.secondaryPublisher = secondaryPublisher;
        this.distribution = (int) (100 / secondaryWeight);
    }

    @Override
    public GattoWisdom wisdomForMood(GattoWisdom.Mood mood) {
        return selectPublisher().wisdomForMood(mood);
    }

    @Override
    public GattoWisdom randomWisdom() {
        return selectPublisher().randomWisdom();
    }

    private GattoWisdomPublisher selectPublisher() {
        var requestId = counter.incrementAndGet();
        var mod = requestId % distribution;
        return mod == 0 ? secondaryPublisher : primaryPublisher;
    }

}
