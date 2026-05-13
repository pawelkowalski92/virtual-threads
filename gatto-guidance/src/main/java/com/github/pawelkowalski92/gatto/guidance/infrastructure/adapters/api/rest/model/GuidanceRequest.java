package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.api.rest.model;

public record GuidanceRequest(Mood mood) {

    public enum Mood {
        LAUGHS,
        FEELS
    }
}
