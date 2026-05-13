package com.github.pawelkowalski92.gatto.image.application.core.model;

public record GattoGuidance(Mood mood,
                            String sentence) {

    public enum Mood {
        PLAYFUL,
        NEUTRAL,
        DEPRESSING
    }

}
