package com.skocur.imagecipher.plugin

import spock.lang.Specification

class PluginLoaderTest extends Specification {

    def "load one plugin that is given by default"() {
        given:
        PluginLoader pluginLoader = new PluginLoader()
        pluginLoader.loadAllPlugins("plugins/")

        expect:
        PluginManager.plugins.size() == 1
        PluginManager.plugins.get(0).pluginName == "LLBE Plugin"

        cleanup:
        PluginManager.plugins.clear()
    }

    def "no plugins should be loaded"() {
        given:
        PluginLoader pluginLoader = new PluginLoader()
        pluginLoader.loadAllPlugins("test/")

        expect:
        PluginManager.plugins.size() == 0

        cleanup:
        PluginManager.plugins.clear()
    }
}
