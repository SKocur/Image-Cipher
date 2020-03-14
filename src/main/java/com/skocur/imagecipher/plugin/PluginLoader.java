package com.skocur.imagecipher.plugin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.imagecipher.icsdk.IcPlugin;
import com.imagecipher.icsdk.PluginInstance;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginLoader {

  static void loadAllPlugins() {
    File[] jars = listJars();

    if (jars == null) {
      return;
    }

    List<URI> jarsUrls = listUris(jars);

    jarsUrls.parallelStream().forEach(PluginLoader::loadPluginFromUri);
  }

  private static void loadPluginFromUri(@NotNull URI uri) {
    try {
      JarFile jarFile = new JarFile(uri.getPath());

      JarEntry jarEntry = (JarEntry) jarFile.getEntry("plugin.yml");

      if (jarEntry == null) {
        System.err.println(
            String.format("Cannot find plugin.yml for %s", uri.toString())
        );
        return;
      }

      PluginConfiguration pluginConfiguration = getYamlMapper()
          .readValue(getYamlContent(jarFile.getInputStream(jarEntry)), PluginConfiguration.class);

      ClassLoader loader = getPluginLoader(uri);

      if (loader == null) {
        System.err.println(
            String.format("Class loader is null, because of malformed uri: %s", uri.toString()));
        return;
      }

      loadPlugin(loader, pluginConfiguration);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void loadPlugin(@NotNull ClassLoader loader,
      @NotNull PluginConfiguration pluginConfiguration) {
    IcPlugin plugin;
    try {
      plugin = (IcPlugin) loader.loadClass(pluginConfiguration.getMainClassPath())
          .newInstance();

      PluginInstance instance = plugin.onPluginLoaded();
      pluginConfiguration.setPluginInstance(instance);

      PluginManager.getPlugins().add(pluginConfiguration);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Nullable
  private static ClassLoader getPluginLoader(@NotNull URI uri) {
    try {
      return new URLClassLoader(
          new URL[]{uri.toURL()},
          PluginManager.class.getClassLoader()
      );
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return null;
  }

  @NotNull
  private static String getYamlContent(@NotNull InputStream inputStream) {
    BufferedReader in = new BufferedReader(
        new InputStreamReader(inputStream));
    StringBuilder yamlContentBuilder = new StringBuilder();
    String line;
    try {
      while ((line = in.readLine()) != null) {
        yamlContentBuilder.append(line).append(System.lineSeparator());
      }
    } catch (IOException e) {
      System.err.println(
          String
              .format("IOException occured while reading contents of yaml file, %s", e.getMessage())
      );
    }

    return yamlContentBuilder.toString();
  }

  @NotNull
  private static ObjectMapper getYamlMapper() {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    return mapper;
  }

  @NotNull
  private static List<URI> listUris(File[] jars) {
    return Arrays.stream(jars)
        .map(File::toURI)
        .collect(Collectors.toList());
  }

  @Nullable
  private static File[] listJars() {
    File dir = new File(PluginManager.PLUGINS_PATH);
    return dir.listFiles((directory, name) -> name.endsWith(".jar"));
  }
}
