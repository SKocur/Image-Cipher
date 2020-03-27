package com.skocur.imagecipher.plugin;

import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginManager {

  static final String PLUGINS_PATH = "plugins/";

  private static List<PluginConfiguration> plugins = new LinkedList<>();

  private static final Logger logger = LogManager.getLogger();

  public static void initialize() {
    logger.info("Initializing PluginLoader");

    PluginLoader pluginLoader = new PluginLoader();
    pluginLoader.loadAllPlugins(PluginManager.PLUGINS_PATH);
  }

  public static List<PluginConfiguration> getPlugins() {
    return plugins;
  }
}
