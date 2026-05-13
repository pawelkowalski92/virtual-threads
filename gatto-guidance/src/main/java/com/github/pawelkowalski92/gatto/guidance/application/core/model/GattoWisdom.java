package com.github.pawelkowalski92.gatto.guidance.application.core.model;

public record GattoWisdom(Mood mood,
                          String sentence) {

    public enum Mood {
        PLAYFUL,
        NEUTRAL,
        DEPRESSING
    }

}
