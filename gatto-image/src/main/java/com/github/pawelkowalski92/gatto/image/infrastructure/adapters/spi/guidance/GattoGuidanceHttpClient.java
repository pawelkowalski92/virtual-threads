package com.github.pawelkowalski92.gatto.image.infrastructure.adapters.spi.guidance;

import com.github.pawelkowalski92.gatto.image.application.core.guidance.GattoGuidanceReceiver;
import com.github.pawelkowalski92.gatto.image.application.core.model.GattoGuidance;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class GattoGuidanceHttpClient implements GattoGuidanceReceiver {

    private final RestClient restClient;

    public GattoGuidanceHttpClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public GattoGuidance askForGuidance() {
        return restClient.get()
                .uri("/gatto-guidance/ask")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(GattoGuidance.class);
    }

}
