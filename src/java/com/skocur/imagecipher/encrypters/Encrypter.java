package com.skocur.imagecipher.encrypters;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Encrypter {

    private int imageWidth;
    private int imageHeight;
    protected String fileName;
    protected BufferedImage image;

    public Encrypter(String fileName) throws IOException {
        this.fileName = fileName;
        this.image = ImageIO.read(new File(fileName));
        this.imageWidth = image.getWidth();
    }

    public abstract void encrypt(String text);

    protected void saveEncryptedData() {
        try {
            File file = new File(fileName);
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
