package com.skocur.imagecipher.tools;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class ColorParser {

  public static int getColor(@NotNull Color color) {
    int R = (int) Math.round(255 * color.getRed());
    int G = (int) Math.round(255 * color.getGreen());
    int B = (int) Math.round(255 * color.getBlue());

    R = (R << 16) & 0x00FF0000;
    G = (G << 8) & 0x0000FF00;
    B = B & 0x000000FF;

    return 0xFF000000 | R | G | B;
  }
}
