package com.skocur.imagecipher.tools.imageprocessing.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class BfsImagePainter {

    public static BufferedImage getBfsPaintedImage(File file, int iterations, Color penColor) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int imageY = image.getHeight();
        int imageX = image.getWidth();
        int startColor = image.getRGB(0, 0);

        Queue<Pixel> queue = new LinkedList<>();
        queue.add(new Pixel(0, 0, startColor));

        int counter = 0;
        while (!queue.isEmpty() && counter < iterations) {
            Pixel pixel = queue.poll();
            pixel.visited = true;
            image.setRGB(pixel.x, pixel.y, penColor.getRGB());

            if (pixel.y < imageY - 1 && image.getRGB(pixel.x, pixel.y + 1) == startColor) {
                queue.add(new Pixel(pixel.x, pixel.y + 1, startColor));
            }

            if (pixel.y > 0 && image.getRGB(pixel.x, pixel.y - 1) == startColor) {
                queue.add(new Pixel(pixel.x, pixel.y - 1, startColor));
            }

            if (pixel.x < imageX - 1 && image.getRGB(pixel.x + 1, pixel.y) == startColor) {
                queue.add(new Pixel(pixel.x + 1, pixel.y, startColor));
            }

            if (pixel.x > 0 && image.getRGB(pixel.x - 1, pixel.y) == startColor) {
                queue.add(new Pixel(pixel.x - 1, pixel.y, startColor));
            }
            counter++;
        }

        return image;
    }

    private static class Pixel {
        int y, x, color;
        boolean visited = false;

        Pixel(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}
