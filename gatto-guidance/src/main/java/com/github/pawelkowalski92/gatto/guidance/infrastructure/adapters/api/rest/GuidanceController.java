package com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.api.rest;

import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.api.rest.model.GuidanceRequest;
import com.github.pawelkowalski92.gatto.guidance.infrastructure.adapters.api.rest.model.GuidanceResponse;
import com.github.pawelkowalski92.gatto.guidance.application.services.GattoGuidanceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gatto-guidance")
public class GuidanceController {

    private final GattoGuidanceService gattoGuidanceService;

    public GuidanceController(GattoGuidanceService gattoGuidanceService) {
        this.gattoGuidanceService = gattoGuidanceService;
    }

    @GetMapping(value = "/ask", produces = MediaType.APPLICATION_JSON_VALUE)
    public GuidanceResponse askGattoForGuidance(@RequestParam(value = "mood", required = false) GuidanceRequest.Mood mood) {
        var guidanceRequest = new GuidanceRequest(mood);
        return gattoGuidanceService.askGattoForGuidance(guidanceRequest);
    }

}
