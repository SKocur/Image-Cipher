package com.skocur.imagecipher.plugin.lib;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class IcEncrypter implements AutoCloseable {
    protected String fileName;
    protected BufferedImage image;

    public abstract void encrypt(String var1);

    public String getFileName() {
        return this.fileName;
    }

    public void setSourceFile(String fileName) throws IOException {
        this.fileName = fileName;
        this.image = ImageIO.read(new File(fileName));
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
