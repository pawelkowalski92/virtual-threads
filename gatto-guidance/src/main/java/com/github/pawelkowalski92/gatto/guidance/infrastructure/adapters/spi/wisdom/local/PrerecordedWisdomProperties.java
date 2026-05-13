package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.local;

import com.github.pawelkowalski92.gatto.guidance.application.core.model.GattoWisdom;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sentences")
public record PrerecordedWisdomProperties(List<String> playful,
                                          List<String> neutral,
                                          List<String> depressing) {

    public List<String> findForMood(GattoWisdom.Mood mood) {
        return switch (mood) {
            case PLAYFUL -> playful();
            case NEUTRAL -> neutral();
            case DEPRESSING -> depressing();
        };
    }

}
