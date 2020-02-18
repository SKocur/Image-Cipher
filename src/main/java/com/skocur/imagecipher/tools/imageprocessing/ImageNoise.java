package com.skocur.imagecipher.tools.imageprocessing;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageNoise {

    private String fileName;
    private BufferedImage image;

    public ImageNoise(@NotNull String fileName) throws IOException {
        this.fileName = fileName;
        this.image = ImageIO.read(new File(fileName));
    }

    /**
     * Randomly shuffles every pixel in image. It has to go through
     * whole file in order to swap everything.
     *
     * @return
     */
    @NotNull
    public BufferedImage createRandomNoise() {
        int sum = 0;
        while (sum < image.getHeight() * image.getWidth()) {
            int x = (int) (Math.random() * image.getWidth());
            int y = (int) (Math.random() * image.getHeight());

            int x2 = (int) (Math.random() * image.getWidth());
            int y2 = (int) (Math.random() * image.getHeight());

            image.setRGB(x, y, image.getRGB(x2, y2));

            sum++;
        }

        return image;
    }

    public void saveNoiseImage(@NotNull BufferedImage image) {
        try {
            File file = new File(fileName.split("\\.")[0] + "_noise.png");
            ImageIO.write(image, "png", file);

            System.out.println("Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
