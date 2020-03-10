package com.skocur.imagecipher.encrypters;

import com.imagecipher.icsdk.annotations.IcAlgorithmSpecification;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

@IcAlgorithmSpecification(algorithmName = "Multi Color Encryption")
public class MultiColorEncryption extends Encrypter {

  public MultiColorEncryption(@NotNull String fileName) throws IOException {
    super(fileName);
  }

  @Override
  public void encrypt(@NotNull String text) {
    int index = 0;
    boolean hasDone = false;

    for (int i = 0; i < image.getHeight() && !hasDone; ++i) {
      for (int j = 0; j < image.getWidth() && !hasDone; ++j) {
        if (index < text.length()) {
          Color color = encryptBlue(text.charAt(index++), j, i);

          image.setRGB(j, i, color.getRGB());
        } else {
          hasDone = true;
        }
      }
    }
  }

  @NotNull
  private Color encryptBlue(char character, int posX, int posY) {
    int argb = image.getRGB(posX, posY);

    int r = (argb >> 16) & 0b11111111;
    int g = (argb >> 8) & 0b11111111;

    int newB = 0;
    if (character < 255) {
      newB = character & 0b11111111;
    }

    return new Color(r, g, newB);
  }
}
