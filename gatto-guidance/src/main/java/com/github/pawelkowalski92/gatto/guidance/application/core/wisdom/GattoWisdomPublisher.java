package com.github.pawelkowalski92.gatto.guidance.application.core.wisdom;

import com.github.pawelkowalski92.gatto.guidance.application.core.model.GattoWisdom;

public interface GattoWisdomPublisher {

    GattoWisdom wisdomForMood(GattoWisdom.Mood mood);

    GattoWisdom randomWisdom();

}
