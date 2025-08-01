package com.imagecipher.app.plugin

import java.io.File
import java.net.URI

object FileLister {

    fun listUris(jars: Array<File>): List<URI> {
        return jars.map { it.toURI() }
    }

    fun listJars(pluginsPath: String): Array<File>? {
        val dir = File(pluginsPath)
        return dir.listFiles { _, name -> name.endsWith(".jar") }
    }
}
