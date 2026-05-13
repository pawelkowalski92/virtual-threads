package com.github.pawelkowalski92.gatto.guidance.application.services;

import com.github.pawelkowalski92.gatto.guidance.application.core.model.GattoWisdom;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.api.rest.model.GuidanceRequest;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.api.rest.model.GuidanceResponse;
import com.github.pawelkowalski92.gatto.guidance.application.core.wisdom.GattoWisdomPublisher;

public class GattoGuidanceService {

    private final GattoWisdomPublisher gattoWisdomPublisher;

    public GattoGuidanceService(GattoWisdomPublisher gattoWisdomPublisher) {
        this.gattoWisdomPublisher = gattoWisdomPublisher;
    }

    public GuidanceResponse askGattoForGuidance(GuidanceRequest guidanceRequest) {
        var mood = determineMood(guidanceRequest.mood());
        var gattoWisdom = mood != null
                ? gattoWisdomPublisher.wisdomForMood(mood)
                : gattoWisdomPublisher.randomWisdom();
        return new GuidanceResponse(
                gattoWisdom.mood().name(),
                gattoWisdom.sentence()
        );
    }

    private GattoWisdom.Mood determineMood(GuidanceRequest.Mood mood) {
        if (mood == null) return null;
        return switch (mood) {
            case LAUGHS -> GattoWisdom.Mood.PLAYFUL;
            case FEELS -> GattoWisdom.Mood.DEPRESSING;
        };
    }

}
