package com.skocur.imagecipher.plugin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.imagecipher.icsdk.IcPlugin;
import com.imagecipher.icsdk.PluginInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class PluginLoader {

    public static void loadAllPlugins() {
        File[] jars = listJars();

        if (jars == null) {
            return;
        }

        List<URI> jarsUrls = listUris(jars);

        jarsUrls.forEach(PluginLoader::loadPluginFromUri);
    }

    private static void loadPluginFromUri(URI uri) {
        try {
            JarFile jarFile = new JarFile(uri.getPath());

            JarEntry jarEntry = (JarEntry) jarFile.getEntry("plugin.yml");
            JarInputStream jarInputStream = new JarInputStream(jarFile.getInputStream(jarEntry));

            PluginConfiguration pluginConfiguration = getYamlMapper().readValue(jarInputStream, PluginConfiguration.class);

            URLClassLoader loader = new URLClassLoader(
                    new URL[]{new URL(pluginConfiguration.getMainClassPath())},
                    PluginManager.class.getClassLoader()
            );

            IcPlugin plugin = (IcPlugin) loader.loadClass(pluginConfiguration.getMainClassPath())
                    .newInstance();
            PluginInstance instance = plugin.onPluginLoaded();
            pluginConfiguration.setPluginInstance(instance);

            PluginManager.getPlugins().add(pluginConfiguration);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static ObjectMapper getYamlMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private static List<URI> listUris(File[] jars) {
        return Arrays.stream(jars)
                .map(File::toURI)
                .collect(Collectors.toList());
    }

    private static File[] listJars() {
        File dir = new File(PluginManager.PLUGINS_PATH);
        return dir.listFiles((directory, name) -> name.endsWith(".jar"));
    }
}
