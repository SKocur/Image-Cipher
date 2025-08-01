package com.imagecipher.app.tools.plugin

import com.imagecipher.app.plugin.PluginLoader
import com.imagecipher.app.plugin.PluginManager
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals

class PluginLoaderTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun shouldNotLoadPluginBecauseOfEmptyPluginYml() {
        // given
        MockJar.createJarFileWithEmptyPluginYaml(File(tempDir, "plugin1.jar"))
        val pluginLoader = PluginLoader()

        // when
        pluginLoader.loadAllPlugins("${tempDir}/")

        // then
        assertEquals(0, PluginManager.getPlugins().size)
    }

    @Test
    fun shouldNotLoadPluginBecauseOfMalformedPluginYml() {
        // given
        val pluginContent = """
            pluginVersion: 1.0
            pluginName: TEST
            >><<><>
            mainClassPath: TEST
        """.trimIndent()
        MockJar.createJarFileWithPluginYaml(File(tempDir, "plugin1.jar"), pluginContent)
        val pluginLoader = PluginLoader()

        // when
        pluginLoader.loadAllPlugins("${tempDir}/")

        // then
        assertEquals(0, PluginManager.getPlugins().size)
    }
}
