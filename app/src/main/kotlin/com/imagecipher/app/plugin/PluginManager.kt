package com.imagecipher.app.plugin

import org.apache.logging.log4j.LogManager

object PluginManager {

    const val PLUGINS_PATH = "plugins/"

    private val plugins: MutableList<PluginConfiguration> = mutableListOf()

    private val logger = LogManager.getLogger()

    fun initialize() {
        logger.info("Initializing PluginLoader")

        val pluginLoader = PluginLoader()
        pluginLoader.loadAllPlugins(PLUGINS_PATH)
    }

    fun getPlugins(): MutableList<PluginConfiguration> {
        return plugins
    }
}
