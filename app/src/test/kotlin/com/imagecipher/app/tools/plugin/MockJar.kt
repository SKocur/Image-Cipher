package com.imagecipher.app.tools.plugin

import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

object MockJar {

    fun createJarFileWithEmptyPluginYaml(file: File) {
        createJarFileWithPluginYaml(file, "")
    }

    fun createJarFileWithPluginYaml(file: File, pluginContent: String) {
        createJarFile(file, PluginFile(ZipEntry("plugin.yml"), pluginContent))
    }

    fun createJarFile(file: File, vararg pluginFiles: PluginFile) {
        try {
            FileOutputStream(file).use { fout ->
                JarOutputStream(fout).use { jarOut ->
                    for (pluginFile in pluginFiles) {
                        jarOut.putNextEntry(pluginFile.zipEntry)
                        jarOut.write(pluginFile.content.toByteArray())
                        jarOut.closeEntry()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    data class PluginFile(val zipEntry: ZipEntry, val content: String)
}
