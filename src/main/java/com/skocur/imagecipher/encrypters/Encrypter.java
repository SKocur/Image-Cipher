package com.skocur.imagecipher.encrypters;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class representing encrypters within Image Cipher. Every encrypter operates only on pixels of
 * loaded image. Output is always saved as soon as encryption is done. Getter methods that return
 * dimensions of image can be used by outside class to calculate capacity of data that are allowed
 * to be stored.
 */
public abstract class Encrypter implements AutoCloseable {

  private int imageWidth;
  protected String fileName;
  protected BufferedImage image;

  /**
   * Basic constructor takes only one parameter that is used to create BufferedImage.
   *
   * @param fileName Path to image file
   */
  public Encrypter(@NotNull String fileName) throws IOException {
    this.fileName = fileName;
    this.image = ImageIO.read(new File(fileName));
    this.imageWidth = image.getWidth();
  }

  public abstract void encrypt(@NotNull String text);

  /**
   * Method that should be invoked after all encryption processes have finished.
   */
  @Override
  public void close() {
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
}
