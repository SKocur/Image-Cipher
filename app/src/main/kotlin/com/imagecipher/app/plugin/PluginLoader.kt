package com.imagecipher.app.plugin

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.imagecipher.app.plugin.lib.IcPlugin
import com.imagecipher.app.plugin.lib.PluginInstance
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.*
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile

class PluginLoader {

    private val mapper: ObjectMapper = ObjectMapper(YAMLFactory()).apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
    }

    fun loadAllPlugins(path: String?) {
        logger.info("Loading all plugins")
        val jars: Array<File>? = FileLister.listJars(path!!)

        if (jars == null) {
            logger.warn("No plugin have been found")
            return
        }

        val jarsUrls: List<URI> = FileLister.listUris(jars)

        jarsUrls.parallelStream().forEach { uri: URI? -> this.loadPluginFromUri(uri!!) }
    }

    private fun loadPluginFromUri(uri: URI) {
        logger.debug("Loading plugin from URI: " + uri.getPath())
        try {
            val jarFile = JarFile(uri.getPath())

            val jarEntry = jarFile.getEntry("plugin.yml") as JarEntry?

            if (jarEntry == null) {
                logger.error(
                    String.format("Cannot find plugin.yml for %s", uri.toString())
                )
                return
            }

            val pluginConfiguration = mapper
                .readValue<PluginConfiguration?>(
                    getYamlContent(jarFile.getInputStream(jarEntry)),
                    PluginConfiguration::class.java
                )

            val loader = getPluginLoader(uri)

            if (loader == null) {
                logger.error(
                    String.format(
                        "Class loader is null, because of malformed uri: %s",
                        uri.toString()
                    )
                )
                return
            }

            loadPlugin(loader, pluginConfiguration)
        } catch (e: MalformedURLException) {
            logger.error(e)
        } catch (e: FileNotFoundException) {
            logger.error(e)
        } catch (e: JsonMappingException) {
            logger.error(e)
        } catch (e: IOException) {
            logger.error(e)
        }
    }

    private fun loadPlugin(
        loader: ClassLoader,
        pluginConfiguration: PluginConfiguration
    ) {
        logger.debug("Loading plugin: " + pluginConfiguration.pluginName)
        try {
            val plugin: IcPlugin = loader.loadClass(pluginConfiguration.mainClassPath)
                .newInstance() as IcPlugin

            val instance: PluginInstance? = plugin.onPluginLoaded()
            pluginConfiguration.pluginInstance = instance

            PluginManager.getPlugins().add(pluginConfiguration)
        } catch (e: InstantiationException) {
            logger.error(e)
        } catch (e: IllegalAccessException) {
            logger.error(e)
        } catch (e: ClassNotFoundException) {
            logger.error(e)
        }
    }

    private fun getPluginLoader(uri: URI): ClassLoader? {
        try {
            return URLClassLoader(
                arrayOf<URL>(uri.toURL()),
                PluginManager::class.java.getClassLoader()
            )
        } catch (e: MalformedURLException) {
            logger.error(e)
        }

        return null
    }

    private fun getYamlContent(inputStream: InputStream): String {
        val `in` = BufferedReader(
            InputStreamReader(inputStream)
        )
        val yamlContentBuilder = StringBuilder()
        var line: String?
        try {
            logger.info("Reading plugin.yaml")
            while ((`in`.readLine().also { line = it }) != null) {
                yamlContentBuilder.append(line).append(System.lineSeparator())
            }
        } catch (e: IOException) {
            logger.error(
                String.format(
                    "IOException occured while reading contents of yaml file, %s",
                    e.message
                )
            )
        }

        return yamlContentBuilder.toString()
    }

    companion object {
        private val logger: Logger = LogManager.getLogger()
    }
}
