package com.skocur.imagecipher.encrypters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class EncrypterManager {

  private static final Logger logger = LogManager.getLogger();

  @Nullable
  public static Encrypter getEncrypter(@NotNull EncrypterType type,
      @NotNull String fileName) {
    try {
      return switch (type) {
        case SINGLE_LEVEL_ENCRYPTION -> new SingleColorEncryption(fileName);
        case MULTI_COLOR_ENCRYPTION -> new MultiColorEncryption(fileName);
        case LOW_LEVEL_BIT_ENCRYPTION -> new LowLevelBitEncryption(fileName);
        case RSA_ENCRYPTION -> new RSAEncryption(fileName);
      };
    } catch (IOException e) {
      logger.error("Cannot create encryption instance: %s\n", e.getMessage());
    }
    return null;
  }
}
