package com.skocur.imagecipher.plugin;

import java.util.LinkedList;
import java.util.List;

public class PluginManager {

    static final String PLUGINS_PATH = "plugins/";

    private static List<PluginConfiguration> plugins = new LinkedList<>();

    public static void initialize() {
        PluginLoader.loadAllPlugins();
    }

    public static List<PluginConfiguration> getPlugins() {
        return plugins;
    }
}
