package com.skocur.imagecipher.tools;

import com.skocur.imagecipher.Main;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Manifest;
import org.jetbrains.annotations.Nullable;

public class ManifestReader {

  @Nullable
  public static Manifest getManifest() {
    String className = Main.class.getSimpleName() + ".class";
    String classPath = Main.class.getResource(className).toString();
    String manifestPath = classPath
        .replace("com/skocur/imagecipher/Main.class", "META-INF/MANIFEST.mf");
    try {
      return new Manifest(new URL(manifestPath).openStream());
    } catch (IOException e) {
      System.err.println("Cannot find manifest file: " + e.getMessage());
    }

    return null;
  }

  @Nullable
  public static Date getBuildTime() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    try {
      Manifest manifest = getManifest();
      if (manifest == null) {
        return null;
      }

      return dateFormat.parse(getManifest().getMainAttributes().getValue("Build-Time"));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return null;
  }
}
