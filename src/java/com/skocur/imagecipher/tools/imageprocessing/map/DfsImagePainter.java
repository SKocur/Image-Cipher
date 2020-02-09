package com.skocur.imagecipher.tools.imageprocessing.map;

import com.skocur.imagecipher.tools.ColorParser;
import com.skocur.imagecipher.tools.imageprocessing.Pixel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class DfsImagePainter {

    /**
     * Depth First Search algorithm used to color image. This specific implementation
     * uses stack, because it will prevent possible StackOverflowError which would
     * occur if it gets too big image to traverse.
     *
     * @param file           Image file
     * @param iterations     Number of iterations
     * @param penColor       Color of ink that will be used to color image
     * @param animationPause Duration of break between iterations
     * @param preview        ImageView that displays live changes done by algorithm
     * @return BufferedImage - it is returned at the end of painting
     * @throws IOException May occur while reading the image file
     */
    public static void paintImage(File file,
                                  int iterations,
                                  Color penColor,
                                  int animationPause,
                                  ImageView preview) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int imageY = image.getHeight();
        int imageX = image.getWidth();
        int startColor = image.getRGB(0, 0);

        Stack<Pixel> stack = new Stack<>();
        stack.push(new Pixel(0, 0, startColor));

        int counter = 0;
        while (!stack.isEmpty() && counter < iterations) {
            Pixel pixel = stack.pop();
            image.setRGB(pixel.x, pixel.y, ColorParser.getColor(penColor));

            if (preview != null) {
                preview.setImage(SwingFXUtils.toFXImage(image, null));
            }

            if (pixel.y < imageY - 1 && image.getRGB(pixel.x, pixel.y + 1) == startColor) {
                stack.push(new Pixel(pixel.x, pixel.y + 1, startColor));
                counter++;
            }

            if (pixel.y > 0 && image.getRGB(pixel.x, pixel.y - 1) == startColor) {
                stack.push(new Pixel(pixel.x, pixel.y - 1, startColor));
                counter++;
            }

            if (pixel.x < imageX - 1 && image.getRGB(pixel.x + 1, pixel.y) == startColor) {
                stack.push(new Pixel(pixel.x + 1, pixel.y, startColor));
                counter++;
            }

            if (pixel.x > 0 && image.getRGB(pixel.x - 1, pixel.y) == startColor) {
                stack.push(new Pixel(pixel.x - 1, pixel.y, startColor));
                counter++;
            }

            try {
                Thread.sleep(animationPause);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
