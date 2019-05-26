package com.skocur.imagecipher.tools.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorFilter {

    public static void getRedOf(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);

        for (int row = 0; row < image.getHeight(); ++row) {
            for (int col = 0; col < image.getWidth(); ++col) {
                int rgb = image.getRGB(col, row);

                Color color = new Color((rgb >> 16) & 255, 0, 0);
                image.setRGB(col, row, color.getRGB());
            }
        }

        saveColorData(file.getName(), "red", image);
    }

    private static void saveColorData(String fileName, String fileTag, BufferedImage image) {
        try {
            File file = new File(fileName.split("\\.")[0] + "_" + fileTag + ".png");
            ImageIO.write(image, "png", file);

            System.out.println("Saved: " + file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
