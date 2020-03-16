package com.skocur.imagecipher.plugin;

import com.skocur.imagecipher.plugin.di.DaggerLoaderComponent;
import com.skocur.imagecipher.plugin.di.LoaderComponent;
import com.skocur.imagecipher.plugin.di.LoaderModule;
import java.util.LinkedList;
import java.util.List;

public class PluginManager {

  static final String PLUGINS_PATH = "plugins/";

  private static List<PluginConfiguration> plugins = new LinkedList<>();

  public static void initialize() {
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
