package com.github.pawelkowalski92.gatto.image.infrastructure.adapters.api.rest;

import com.github.pawelkowalski92.gatto.image.application.services.renderer.GattoGuidanceRenderer;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/gatto-image")
public class ImageRenderingController {

    private final GattoGuidanceRenderer gattoGuidanceRenderer;

    public ImageRenderingController(GattoGuidanceRenderer gattoGuidanceRenderer) {
        this.gattoGuidanceRenderer = gattoGuidanceRenderer;
    }

    @GetMapping(value = "/guidance", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> generateGuidanceImage() throws Exception {
        var renderer = gattoGuidanceRenderer.renderGuidanceImage();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(renderer::renderTo);
    }

}
