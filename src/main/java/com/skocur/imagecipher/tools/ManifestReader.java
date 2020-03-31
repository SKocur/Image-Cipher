package com.skocur.imagecipher.tools;

import com.jcabi.manifests.Manifests;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ManifestReader {

  private static final Logger logger = LogManager.getLogger();

  @Nullable
  public static Date getBuildTime() {
    logger.info("Getting build time");
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    try {
      String time = Manifests.read("Build-Time");
      if (time == null || time.equals("")) {
        logger.warn("Build-Time is empty in MANIFEST");
        return null;
      }

      return dateFormat.parse(time);
    } catch (ParseException e) {
      logger.error(e);
    }

    return null;
  }
}
