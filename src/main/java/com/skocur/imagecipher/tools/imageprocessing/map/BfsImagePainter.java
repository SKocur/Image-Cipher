package com.skocur.imagecipher.tools.imageprocessing.map;

import com.skocur.imagecipher.tools.ColorParser;
import com.skocur.imagecipher.tools.imageprocessing.Pixel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class BfsImagePainter {

    /**
     * Breadth First Search algorithm to color pixels in image.
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
                                  ImageView preview,
                                  Point point) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int imageY = image.getHeight();
        int imageX = image.getWidth();

        double scaleX = preview.getFitWidth() > 0 ? (double) imageX / preview.getFitWidth() : 1;
        double scaleY = preview.getFitHeight() > 0 ? (double) imageY / preview.getFitHeight() : 1;

        int startPointX = (int) (point.x * scaleX);
        int startPointY = (int) (point.y * scaleY);

        int startColor = image.getRGB(startPointX, startPointY);

        Queue<Pixel> queue = new LinkedList<>();
        queue.add(new Pixel(startPointX, startPointY, startColor));

        int counter = 0;
        while (!queue.isEmpty() && counter < iterations) {
            Pixel pixel = queue.poll();
            image.setRGB(pixel.x, pixel.y, ColorParser.getColor(penColor));

            preview.setImage(SwingFXUtils.toFXImage(image, null));

            if (pixel.y < imageY - 1
                    && image.getRGB(pixel.x, pixel.y + 1) == startColor) {
                queue.add(new Pixel(pixel.x, pixel.y + 1, startColor));
                counter++;
            }

            if (pixel.y > 0
                    && image.getRGB(pixel.x, pixel.y - 1) == startColor) {
                queue.add(new Pixel(pixel.x, pixel.y - 1, startColor));
                counter++;
            }

            if (pixel.x < imageX - 1
                    && image.getRGB(pixel.x + 1, pixel.y) == startColor) {
                queue.add(new Pixel(pixel.x + 1, pixel.y, startColor));
            }

            if (pixel.x > 0
                    && image.getRGB(pixel.x - 1, pixel.y) == startColor) {
                queue.add(new Pixel(pixel.x - 1, pixel.y, startColor));
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
