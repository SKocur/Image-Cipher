package com.skocur.imagecipher.plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PluginLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldNotLoadPluginBecauseOfEmptyPluginYml() {
        // given
        MockJar.createJarFileWithEmptyPluginYaml(new File(tempDir.toFile(), "plugin1.jar"));
        PluginLoader pluginLoader = new PluginLoader();

        // when
        pluginLoader.loadAllPlugins(tempDir.toString() + "/");

        // then
        assertEquals(0, PluginManager.getPlugins().size());
    }

    @Test
    void shouldNotLoadPluginBecauseOfMalformedPluginYml() {
        // given
        String pluginContent = """
                pluginVersion: 1.0
                pluginName: TEST
                >><<><>
                mainClassPath: TEST
                """;
        MockJar.createJarFileWithPluginYaml(new File(tempDir.toFile(), "plugin1.jar"), pluginContent);
        PluginLoader pluginLoader = new PluginLoader();

        // when
        pluginLoader.loadAllPlugins(tempDir.toString() + "/");

        // then
        assertEquals(0, PluginManager.getPlugins().size());
    }

    @Test
    void shouldNotLoadPluginBecauseOfNotExistingClass() {
        // given
        String pluginContent = """
                pluginVersion: 1.0
                pluginName: TEST
                mainClassPath: TEST
                """;
        MockJar.createJarFileWithPluginYaml(new File(tempDir.toFile(), "plugin1.jar"), pluginContent);
        PluginLoader pluginLoader = new PluginLoader();

        // when
        pluginLoader.loadAllPlugins(tempDir.toString() + "/");

        // then
        assertEquals(0, PluginManager.getPlugins().size());
    }

    @Test
    void shouldNotLoadPluginBecauseOfLackOfPluginYml() {
        // given
        MockJar.createJarFile(new File(tempDir.toFile(), "plugin1.jar"));
        PluginLoader pluginLoader = new PluginLoader();

        // when
        pluginLoader.loadAllPlugins(tempDir.toString() + "/");

        // then
        assertEquals(0, PluginManager.getPlugins().size());
    }

    @Test
    void shouldLoadNoPlugins() {
        // given
        PluginLoader pluginLoader = new PluginLoader();

        // when
        pluginLoader.loadAllPlugins("test/");

        // then
        assertEquals(0, PluginManager.getPlugins().size());
    }

    @AfterEach
    void cleanup() {
        PluginManager.getPlugins().clear();
    }
}
