package com.skocur.imagecipher.tools;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ColorParser {

  private static final Logger logger = LogManager.getLogger();

  public static int getColor(@NotNull Color color) {
    logger.debug("Parsing color: " + color.toString());
    int R = (int) Math.round(255 * color.getRed());
    int G = (int) Math.round(255 * color.getGreen());
    int B = (int) Math.round(255 * color.getBlue());

    R = (R << 16) & 0x00FF0000;
    G = (G << 8) & 0x0000FF00;
    B = B & 0x000000FF;

    return 0xFF000000 | R | G | B;
  }
}
