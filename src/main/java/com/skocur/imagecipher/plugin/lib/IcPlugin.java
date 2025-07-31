package com.skocur.imagecipher.plugin.lib;

public interface IcPlugin {
    PluginInstance onPluginLoaded();

    void onPluginShutdown();
}