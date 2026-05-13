package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.spi.wisdom.ai;

import com.github.pawelkowalski92.gatto.guidance.application.core.wisdom.GattoWisdomPublisher;
import com.github.pawelkowalski92.gatto.guidance.application.core.model.GattoWisdom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.Resource;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class PersonifiedGattoMeowelhoPublisher implements GattoWisdomPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ChatClient gattoMeowelhoPersona;

    private final PromptTemplate wisdomForMoodTemplate;
    private final Prompt randomWisdom;

    public PersonifiedGattoMeowelhoPublisher(ChatClient gattoMeowelhoPersona,
                                             Resource wisdomForMoodSource,
                                             Resource randomWisdomSource) {
        this.gattoMeowelhoPersona = gattoMeowelhoPersona;
        wisdomForMoodTemplate = new PromptTemplate(wisdomForMoodSource);
        randomWisdom = new Prompt(new UserMessage(randomWisdomSource));
    }

    @Override
    public GattoWisdom wisdomForMood(GattoWisdom.Mood mood) {
        LOGGER.info("Requesting lord Gatto Meow'ehlo himself for piece of his wisdom for specific mood: {}", mood);
        var moodInterpretation = switch (mood) {
            case PLAYFUL -> "zabawne";
            case NEUTRAL -> "stoickie";
            case DEPRESSING -> "melancholijne";
        };

        var wisdomForMood = wisdomForMoodTemplate.create(Map.of(
                "mood", moodInterpretation
        ));
        return askGattoMeowelhoAbout(wisdomForMood);
    }

    @Override
    public GattoWisdom randomWisdom() {
        LOGGER.info("Requesting lord Gatto Meow'ehlo himself for piece of his magnificient wisdom");
        return askGattoMeowelhoAbout(randomWisdom);
    }

    private GattoWisdom askGattoMeowelhoAbout(Prompt query) {
        return gattoMeowelhoPersona.prompt(query)
                .call()
                .entity(GattoWisdom.class);
    }

}
