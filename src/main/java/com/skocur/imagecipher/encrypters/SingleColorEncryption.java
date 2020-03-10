package com.skocur.imagecipher.encrypters;

import com.imagecipher.icsdk.annotations.IcAlgorithmSpecification;
import com.skocur.imagecipher.Config;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

@IcAlgorithmSpecification(algorithmName = "Single Color Encryption")
public class SingleColorEncryption extends Encrypter {

  private ArrayList<Integer> asciiChars;

  public SingleColorEncryption(@NotNull String fileName) throws IOException {
    super(fileName);

    asciiChars = new ArrayList<>();
  }

  @Override
  public void encrypt(@NotNull String text) {
    for (char c : text.toCharArray()) {
      asciiChars.add((int) c);
    }

    for (int i = 0, index = 0; i < getImageWidth(); i++) {
      if (i % Config.SPACING_CIPHER == 0 && index < asciiChars.size()) {
        Color greenShade = new Color(1, asciiChars.get(index), 1);
        image.setRGB(i, Config.IMAGE_MARGIN_TOP, greenShade.getRGB());
        index++;
      }
    }
  }
}
