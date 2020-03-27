package com.skocur.imagecipher.plugin;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileLister {

  @NotNull
  public static List<URI> listUris(File[] jars) {
    return Arrays.stream(jars)
        .map(File::toURI)
        .collect(Collectors.toList());
  }

  @Nullable
  public static File[] listJars(String pluginsPath) {
    File dir = new File(pluginsPath);
    return dir.listFiles((directory, name) -> name.endsWith(".jar"));
  }
}
