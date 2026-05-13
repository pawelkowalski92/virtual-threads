package com.github.pawelkowalski92.gatto.image.application.services.renderer;

import com.github.pawelkowalski92.gatto.image.application.core.DataRenderer;
import com.github.pawelkowalski92.gatto.image.application.core.guidance.GattoGuidanceReceiver;
import com.github.pawelkowalski92.gatto.image.application.core.model.GattoGuidance;
import com.github.pawelkowalski92.gatto.image.application.services.generator.GattoImageGeneratorFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;

public class SingleThreadRenderer implements GattoGuidanceRenderer {

    private static final Path IMAGES_PATH = Path.of("images");

    private final GattoGuidanceReceiver gattoGuidance;
    private final GattoImageGeneratorFactory imageGeneratorFactory;
    private final String signature;

    public SingleThreadRenderer(GattoGuidanceReceiver gattoGuidance,
                                GattoImageGeneratorFactory imageGeneratorFactory,
                                String signature) {
        this.gattoGuidance = gattoGuidance;
        this.imageGeneratorFactory = imageGeneratorFactory;
        this.signature = signature;
    }

    public DataRenderer renderGuidanceImage() throws Exception {
        var guidance = gattoGuidance.askForGuidance();
        return doRenderGuidance(guidance);
    }

    protected DataRenderer doRenderGuidance(GattoGuidance guidance) throws IOException {
        var image = findGattoImage(guidance);

        try (var imageGenerator = imageGeneratorFactory.createForImage(image)) {
            imageGenerator.withText(guidance.sentence());
            imageGenerator.withSignature(signature);
            return imageGenerator.generate();
        }
    }

    private Resource findGattoImage(GattoGuidance guidance) {
        var imageName = "gatto-" + guidance.mood().name().toLowerCase() + ".png";
        return new ClassPathResource(
                IMAGES_PATH.resolve(imageName).toString()
        );
    }

}
