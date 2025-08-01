package com.imagecipher.app.plugin.lib

interface IcPlugin {
    fun onPluginLoaded(): PluginInstance

    fun onPluginShutdown()
}
