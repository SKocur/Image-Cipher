package com.skocur.imagecipher.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagecipher.icsdk.IcPlugin;
import com.imagecipher.icsdk.PluginInstance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginLoader {

  private static final Logger logger = LogManager.getLogger();

  @Inject
  @Named("YamlMapper")
  public ObjectMapper mapper;

  void loadAllPlugins() {
    logger.info("Loading all plugins");
    File[] jars = FileLister.listJars(PluginManager.PLUGINS_PATH);

    if (jars == null) {
      logger.warn("No plugin have been found");
      return;
    }

    List<URI> jarsUrls = FileLister.listUris(jars);

    jarsUrls.parallelStream().forEach(this::loadPluginFromUri);
  }

  private void loadPluginFromUri(@NotNull URI uri) {
    logger.debug("Loading plugin from URI: " + uri.getPath());
    try {
      JarFile jarFile = new JarFile(uri.getPath());

      JarEntry jarEntry = (JarEntry) jarFile.getEntry("plugin.yml");

      if (jarEntry == null) {
        logger.error(
            String.format("Cannot find plugin.yml for %s", uri.toString())
        );
        return;
      }

      PluginConfiguration pluginConfiguration = mapper
          .readValue(getYamlContent(jarFile.getInputStream(jarEntry)), PluginConfiguration.class);

      ClassLoader loader = getPluginLoader(uri);

      if (loader == null) {
        logger.error(
            String.format("Class loader is null, because of malformed uri: %s", uri.toString()));
        return;
      }

      loadPlugin(loader, pluginConfiguration);
    } catch (MalformedURLException e) {
      logger.error(e);
    } catch (FileNotFoundException e) {
      logger.error(e);
    } catch (IOException e) {
      logger.error(e);
    }
  }

  private void loadPlugin(@NotNull ClassLoader loader,
      @NotNull PluginConfiguration pluginConfiguration) {
    logger.debug("Loading plugin: " + pluginConfiguration.getPluginName());
    IcPlugin plugin;
    try {
      plugin = (IcPlugin) loader.loadClass(pluginConfiguration.getMainClassPath())
          .newInstance();

      PluginInstance instance = plugin.onPluginLoaded();
      pluginConfiguration.setPluginInstance(instance);

      PluginManager.getPlugins().add(pluginConfiguration);
    } catch (InstantiationException e) {
      logger.error(e);
    } catch (IllegalAccessException e) {
      logger.error(e);
    } catch (ClassNotFoundException e) {
      logger.error(e);
    }
  }

  @Nullable
  private ClassLoader getPluginLoader(@NotNull URI uri) {
    try {
      return new URLClassLoader(
          new URL[]{uri.toURL()},
          PluginManager.class.getClassLoader()
      );
    } catch (MalformedURLException e) {
      logger.error(e);
    }

    return null;
  }

  @NotNull
  private String getYamlContent(@NotNull InputStream inputStream) {
    BufferedReader in = new BufferedReader(
        new InputStreamReader(inputStream));
    StringBuilder yamlContentBuilder = new StringBuilder();
    String line;
    try {
      logger.info("Reading plugin.yaml");
      while ((line = in.readLine()) != null) {
        yamlContentBuilder.append(line).append(System.lineSeparator());
      }
    } catch (IOException e) {
      logger.error(
          String
              .format("IOException occured while reading contents of yaml file, %s", e.getMessage())
      );
    }

    return yamlContentBuilder.toString();
  }
}
