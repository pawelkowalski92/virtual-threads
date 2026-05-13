package com.github.pawelkowalski92.gatto.image.application.services.generator;

import com.github.pawelkowalski92.gatto.image.application.core.DataRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

import static java.text.BreakIterator.getLineInstance;

public class GattoImageGenerator implements AutoCloseable {

    private static final String IMAGE_FORMAT = "PNG";

    private static class Paddings {

        private static final float LEFT = 500f;
        private static final float RIGHT = 40f;
        private static final float BOTTOM = 200f;
        private static final float SIGNATURE_SPACER = 100f;

    }

    private static class Fonts {

        private static final Font PARAGRAPH = new Font("ComicSans", Font.ITALIC, 28);
        private static final Font SIGNATURE = new Font("ComicSans", Font.BOLD, 36);

    }

    private final BufferedImage image;
    private final Graphics2D renderer;

    private final TextRenderer textRenderer;

    public GattoImageGenerator(BufferedImage image) {
        this.image = image;
        this.renderer = image.createGraphics();
        renderer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.textRenderer = new TextRenderer();
    }

    public void withText(String... texts) {
        for (var text : texts) textRenderer.withText(text);
    }

    public void withSignature(String signature) {
        textRenderer.withSignature(signature);
    }

    public DataRenderer generate() {
        textRenderer.render();
        return outputStream -> ImageIO.write(image, IMAGE_FORMAT, outputStream);
    }

    private class TextRenderer {

        private final float wrappingWidth = image.getWidth() - Paddings.LEFT - Paddings.RIGHT;
        private float totalHeight = 0f;

        private final List<TextLayout> layouts = new ArrayList<>();
        private TextLayout signature;

        void withText(String text) {
            var aText = new AttributedString(text);
            aText.addAttribute(TextAttribute.FONT, Fonts.PARAGRAPH);
            aText.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);

            var textIterator = aText.getIterator();
            var textWrapper = new LineBreakMeasurer(textIterator, getLineInstance(), renderer.getFontRenderContext());

            while (textWrapper.getPosition() < textIterator.getEndIndex()) {
                var layout = textWrapper.nextLayout(wrappingWidth);
                layouts.add(layout);
                totalHeight += layout.getAscent() + layout.getDescent() + layout.getLeading();
            }
        }

        void withSignature(String text) {
            var aText = new AttributedString("~" + text);
            aText.addAttribute(TextAttribute.FONT, Fonts.SIGNATURE);
            aText.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);

            signature = new TextLayout(aText.getIterator(), renderer.getFontRenderContext());
            totalHeight += signature.getAscent() + signature.getDescent() + signature.getLeading() + Paddings.SIGNATURE_SPACER;
        }

        void render() {
            float x, y = image.getHeight() - Paddings.BOTTOM - totalHeight;
            for (var layout : layouts) {
                x = image.getWidth() - Paddings.RIGHT - layout.getVisibleAdvance();
                y += layout.getAscent();
                layout.draw(renderer, x, y);
                y += layout.getDescent() + layout.getLeading();
            }
            if (signature == null) return;
            y += Paddings.SIGNATURE_SPACER;
            x = image.getWidth() - Paddings.RIGHT - signature.getVisibleAdvance();
            signature.draw(renderer, x, y);
        }

    }

    @Override
    public void close() {
        renderer.dispose();
    }

}
