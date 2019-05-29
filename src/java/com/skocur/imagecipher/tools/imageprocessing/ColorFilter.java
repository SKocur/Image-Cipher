package com.skocur.imagecipher.tools.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorFilter {

    public static BufferedImage getColorOf(File file, int option) throws IOException {
        BufferedImage image = ImageIO.read(file);

        int bitShift = 16;
        switch (option) {
            case 2:
                bitShift = 8;
                break;
            case 3:
                bitShift = 0;
                break;
        }

        for (int row = 0; row < image.getHeight(); ++row) {
            for (int col = 0; col < image.getWidth(); ++col) {
                int rgb = image.getRGB(col, row);

                Color color = new Color((rgb >> bitShift) & 255, 0, 0);
                if (option == 2) {
                    color = new Color(0, (rgb >> bitShift) & 255, 0);
                } else if (option == 3) {
                    color = new Color(0, 0, (rgb >> bitShift) & 255);
                }

                image.setRGB(col, row, color.getRGB());
            }
        }

        return image;
    }

    /**
     * Method that filter color from image. Option of filtering is based on option
     * fetched from command line argument. Default filter is to RED.
     *
     * @param file Path to image
     * @param option Option of filtering
     * @throws IOException
     */
    public static void getColorAndSave(File file, int option) throws IOException {
        String tag = "red";

        switch (option) {
            case 2:
                tag = "green";
                break;
            case 3:
                tag = "blue";
                break;
        }

        saveColorData(file.getName(), tag, getColorOf(file, option));
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
