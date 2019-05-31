package com.skocur.imagecipher.tools.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageNoise {

    private String fileName;
    private BufferedImage image;

    public ImageNoise(String fileName) throws IOException {
        this.fileName = fileName;
        this.image = ImageIO.read(new File(fileName));
    }

    public BufferedImage createRandomNoise() {
        int[][] pixels = new int[image.getHeight()][image.getWidth()];
        for (int row = 0; row < pixels.length; ++row) {
            for (int col = 0; col < pixels[row].length; ++col) {
                pixels[row][col] = image.getRGB(col, row);
            }
        }

        int sum = 0;
        while (sum < pixels.length * pixels[0].length) {
            int x = (int) (Math.random() * image.getWidth());
            int y = (int) (Math.random() * image.getHeight());
            int x2 = (int) (Math.random() * image.getWidth());
            int y2 = (int) (Math.random() * image.getHeight());

            if (pixels[y][x] != 0) {
                image.setRGB(x, y, pixels[y2][x2]);
                pixels[y][x] = 0;

                sum++;
            }
        }

        return image;
    }

    public void saveNoiseImage(BufferedImage image) {
        try {
            File file = new File(fileName.split("\\.")[0] + "_noise.png");
            ImageIO.write(image, "png", file);

            System.out.println("Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
