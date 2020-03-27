package com.skocur.imagecipher.plugin

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class PluginLoaderTest extends Specification {

    @Rule
    TemporaryFolder testFolder = new TemporaryFolder()

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

    def "plugin should not be loaded, because of empty plugin.yml"() {
        given:
        MockJar.createJarFileWithEmptyPluginYaml(testFolder.newFile("plugin1.jar"))

        PluginLoader pluginLoader = new PluginLoader()
        pluginLoader.loadAllPlugins(testFolder.getRoot().getPath() + "/")

        expect:
        PluginManager.plugins.size() == 0

        cleanup:
        PluginManager.plugins.clear()
    }

    def "plugin should not be loaded, because of malformed plugin.yml"() {
        given:
        String pluginContent = '''
            pluginVersion: 1.0
            pluginName: TEST
            >><<><>
            mainClassPath: TEST
            '''
        MockJar.createJarFileWithPluginYaml(testFolder.newFile("plugin1.jar"), pluginContent)

        PluginLoader pluginLoader = new PluginLoader()
        pluginLoader.loadAllPlugins(testFolder.getRoot().getPath() + "/")

        expect:
        PluginManager.plugins.size() == 0

        cleanup:
        PluginManager.plugins.clear()
    }

    def "plugin should not be loaded, because of not existing class"() {
        given:
        String pluginContent = '''
            pluginVersion: 1.0
            pluginName: TEST
            mainClassPath: TEST
            '''
        MockJar.createJarFileWithPluginYaml(testFolder.newFile("plugin1.jar"), pluginContent)

        PluginLoader pluginLoader = new PluginLoader()
        pluginLoader.loadAllPlugins(testFolder.getRoot().getPath() + "/")

        expect:
        PluginManager.plugins.size() == 0

        cleanup:
        PluginManager.plugins.clear()
    }

    def "plugin should not be loaded, because of lack of plugin.yml"() {
        given:
        MockJar.createJarFile(testFolder.newFile("plugin1.jar"))

        PluginLoader pluginLoader = new PluginLoader()
        pluginLoader.loadAllPlugins(testFolder.getRoot().getPath() + "/")

        expect:
        PluginManager.plugins.size() == 0

        cleanup:
        PluginManager.plugins.clear()
    }

    def "load one plugin that is given by default using plugin manager"() {
        given:
        PluginManager.initialize()

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
