package com.github.pawelkowalski92.gatto.image.application.services.generator;

import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.io.IOException;

public class GattoImageGeneratorFactory {

    public GattoImageGenerator createForImage(Resource imageSource) throws IOException {
        try (var source = imageSource.getInputStream()) {
            var image = ImageIO.read(source);
            return new GattoImageGenerator(image);
        }
    }

}
