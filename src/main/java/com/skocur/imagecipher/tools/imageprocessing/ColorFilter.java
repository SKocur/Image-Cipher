package com.skocur.imagecipher.tools.imageprocessing;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that is responsible for processing data from image. This specific one gets color from every
 * pixel from image and after this process this filtered data can be saved or returned to GUI in
 * order to be displayed.
 */
public class ColorFilter {

  /**
   * Returns BufferedImage object that is in one color. Takes option parameter which describes color
   * value of filtering: 1 - Red (default) 2 - Green 3 - Blue
   * <p>
   * Method goes through whole image changing every pixel it meets to chosen color. This filtering
   * option influences bitwise operation, which is used to fetch specific color from pixel.
   *
   * @param file Image that will be processed
   * @param colorMode Mode of color filtering
   * @return Buffered Image - Processed Image
   * @throws IOException Thrown when file cannot be found
   */
  @NotNull
  public static BufferedImage getColorOf(File file, FilteringColorMode colorMode)
      throws IOException {
    BufferedImage image = ImageIO.read(file);

    int bitShift = 16;
    switch (colorMode) {
      case GREEN:
        bitShift = 8;
        break;
      case BLUE:
        bitShift = 0;
        break;
    }

    for (int row = 0; row < image.getHeight(); ++row) {
      for (int col = 0; col < image.getWidth(); ++col) {
        int rgb = image.getRGB(col, row);

        Color color = new Color((rgb >> bitShift) & 255, 0, 0);
        if (colorMode == FilteringColorMode.GREEN) {
          color = new Color(0, (rgb >> bitShift) & 255, 0);
        } else if (colorMode == FilteringColorMode.BLUE) {
          color = new Color(0, 0, (rgb >> bitShift) & 255);
        }

        image.setRGB(col, row, color.getRGB());
      }
    }

    return image;
  }

  /**
   * Method that filter color from image. Option of filtering is based on option fetched from
   * command line argument. Default filter is to RED.
   *
   * @param file Path to image
   * @param colorMode Option of filtering
   */
  public static void getColorAndSave(@NotNull File file, FilteringColorMode colorMode)
      throws IOException {
    String tag = "red";

    switch (colorMode) {
      case GREEN:
        tag = "green";
        break;
      case BLUE:
        tag = "blue";
        break;
    }

    saveColorData(file.getName(), tag, getColorOf(file, colorMode));
  }

  private static void saveColorData(@NotNull String fileName,
      @NotNull String fileTag,
      @NotNull BufferedImage image) {
    try {
      File file = new File(fileName.split("\\.")[0] + "_" + fileTag + ".png");
      ImageIO.write(image, "png", file);

      System.out.println("Saved: " + file.getPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
