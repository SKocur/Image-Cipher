package com.skocur.imagecipher.encrypters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class EncrypterManager {

  @Nullable
  public static Encrypter getEncrypter(@NotNull EncrypterType type,
      @NotNull String fileName) {
    try {
      switch (type) {
        case SINGLE_LEVEL_ENCRYPTION:
          return new SingleColorEncryption(fileName);
        case MULTI_COLOR_ENCRYPTION:
          return new MultiColorEncryption(fileName);
        case LOW_LEVEL_BIT_ENCRYPTION:
          return new LowLevelBitEncryption(fileName);
        case RSA_ENCRYPTION:
          return new RSAEncryption(fileName);
      }
    } catch (IOException e) {
      System.err.format("Cannot create encryption instance: %s", e.getMessage());
    }
    return null;
  }
}
