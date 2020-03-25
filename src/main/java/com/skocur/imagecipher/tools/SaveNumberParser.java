package com.skocur.imagecipher.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class SaveNumberParser {

  private static final Logger logger = LogManager.getLogger();

  public static int getParsedNumber(@Nullable String text) {
    logger.debug("Parsing number: " + text);
    int res;
    try {
      res = Integer.parseInt(text);
    } catch (NumberFormatException e) {
      logger.warn("Invalid number format, returning 0");
      return 0;
    }

    return res;
  }
}
