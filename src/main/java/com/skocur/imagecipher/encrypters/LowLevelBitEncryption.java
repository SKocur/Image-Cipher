package com.skocur.imagecipher.encrypters;

import com.skocur.imagecipher.plugin.lib.annotations.IcAlgorithmSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

@IcAlgorithmSpecification(algorithmName = "Low Level Bit Encryption")
public class LowLevelBitEncryption extends Encrypter {

  private int row;
  private int col;

  private static final Logger logger = LogManager.getLogger();

  public LowLevelBitEncryption(@NotNull String fileName) throws IOException {
    super(fileName);
  }

  @Override
  public void encrypt(@NotNull String text) {
    logger.debug("Encrypting: " + text);
    for (char c : text.toCharArray()) {
      encryptByte(c);
    }

    // Resetting values of cursor coordinates
    col = 0;
    row = 0;
  }

  private void encryptByte(char character) {
    for (int i = 0; i < 8; ++i) {
      encryptBitCharacter((character >> i) & 0b1);
    }
  }

  private void encryptBitCharacter(int c) {
    if (col < image.getWidth() - 1) {
      int mask = 0b11111111;
      int rgb = image.getRGB(col, row);
      int r = (rgb >> 16) & mask;
      int g = (rgb >> 8) & mask;

      int b = image.getRGB(col + 1, row) & mask;
      b -= c; //TODO What if user add white/bright image to encrypt (b would be equal 0)?

      Color color = new Color(r, g, b);
      image.setRGB(col, row, color.getRGB());

      col += 2;
    } else {
      row++;
      col = 0;
    }
  }
}
