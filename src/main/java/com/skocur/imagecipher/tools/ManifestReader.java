package com.skocur.imagecipher.tools;

import com.skocur.imagecipher.Main;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Manifest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ManifestReader {

  private static final Logger logger = LogManager.getLogger();

  @Nullable
  public static Manifest getManifest() {
    logger.info("Getting manifest");
    String className = Main.class.getSimpleName() + ".class";
    String classPath = Main.class.getResource(className).toString();
    String manifestPath = classPath
        .replace("com/skocur/imagecipher/Main.class", "META-INF/MANIFEST.mf");
    try {
      logger.debug("Manifest path: " + manifestPath);
      return new Manifest(new URL(manifestPath).openStream());
    } catch (IOException e) {
      logger.error("Cannot find manifest file: " + e.getMessage());
    }

    return null;
  }

  @Nullable
  public static Date getBuildTime() {
    logger.info("Getting build time");
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    try {
      Manifest manifest = getManifest();
      if (manifest == null) {
        logger.warn("Manifest is null");
        return null;
      }

      return dateFormat.parse(getManifest().getMainAttributes().getValue("Build-Time"));
    } catch (ParseException e) {
      logger.error(e);
    }

    return null;
  }
}
