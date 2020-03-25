package com.skocur.imagecipher.plugin;

import com.skocur.imagecipher.plugin.di.DaggerLoaderComponent;
import com.skocur.imagecipher.plugin.di.LoaderComponent;
import com.skocur.imagecipher.plugin.di.LoaderModule;
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

    LoaderComponent loaderComponent = DaggerLoaderComponent.builder()
        .loaderModule(new LoaderModule()).build();
    loaderComponent.inject(pluginLoader);

    pluginLoader.loadAllPlugins();
  }

  public static List<PluginConfiguration> getPlugins() {
    return plugins;
  }
}
