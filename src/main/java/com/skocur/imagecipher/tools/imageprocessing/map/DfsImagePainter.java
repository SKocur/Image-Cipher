package com.skocur.imagecipher.tools.imageprocessing.map;

import com.skocur.imagecipher.tools.ColorParser;
import com.skocur.imagecipher.tools.imageprocessing.Pixel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class DfsImagePainter {

  private static final Logger logger = LogManager.getLogger();

  /**
   * Depth First Search algorithm used to color image. This specific implementation uses stack,
   * because it will prevent possible StackOverflowError which would occur if it gets too big image
   * to traverse.
   *
   * @param file Image file
   * @param iterations Number of iterations
   * @param penColor Color of ink that will be used to color image
   * @param animationPause Duration of break between iterations
   * @param preview ImageView that displays live changes done by algorithm
   * @return BufferedImage - it is returned at the end of painting
   * @throws IOException May occur while reading the image file
   */
  public static void paintImage(@NotNull File file,
      int iterations,
      @NotNull Color penColor,
      int animationPause,
      @NotNull ImageView preview,
      @NotNull Point point) throws IOException {
    logger.debug("DFS painting image, " + point.toString());
    BufferedImage image = ImageIO.read(file);
    int imageY = image.getHeight();
    int imageX = image.getWidth();

    double scaleX = preview.getFitWidth() > 0 ? (double) imageX / preview.getFitWidth() : 1;
    double scaleY = preview.getFitHeight() > 0 ? (double) imageY / preview.getFitHeight() : 1;

    int startPointX = (int) (point.x * scaleX);
    int startPointY = (int) (point.y * scaleY);

    int startColor = image.getRGB(startPointX, startPointY);

    Stack<Pixel> stack = new Stack<>();
    stack.push(new Pixel(startPointX, startPointY, startColor));

    int counter = 0;
    while (!stack.isEmpty() && counter < iterations) {
      Pixel pixel = stack.pop();
      image.setRGB(pixel.x, pixel.y, ColorParser.getColor(penColor));

      if (preview != null) {
        preview.setImage(SwingFXUtils.toFXImage(image, null));
      }

      if (pixel.y < imageY - 1 && image.getRGB(pixel.x, pixel.y + 1) == startColor) {
        stack.push(new Pixel(pixel.x, pixel.y + 1, startColor));
        counter++;
      }

      if (pixel.y > 0 && image.getRGB(pixel.x, pixel.y - 1) == startColor) {
        stack.push(new Pixel(pixel.x, pixel.y - 1, startColor));
        counter++;
      }

      if (pixel.x < imageX - 1 && image.getRGB(pixel.x + 1, pixel.y) == startColor) {
        stack.push(new Pixel(pixel.x + 1, pixel.y, startColor));
        counter++;
      }

      if (pixel.x > 0 && image.getRGB(pixel.x - 1, pixel.y) == startColor) {
        stack.push(new Pixel(pixel.x - 1, pixel.y, startColor));
        counter++;
      }

      try {
        Thread.sleep(animationPause);
      } catch (InterruptedException e) {
        logger.debug("Interrupting DFS painting thread: " + e.getMessage());
        return;
      }
    }
  }
}
