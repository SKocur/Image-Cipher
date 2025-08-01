package com.imagecipher.app.plugin

import com.fasterxml.jackson.annotation.JsonProperty
import com.imagecipher.app.plugin.lib.PluginInstance

class PluginConfiguration {

    @JsonProperty("pluginVersion")
    var pluginVersion: String? = null

    @JsonProperty("pluginName")
    var pluginName: String? = null

    @JsonProperty("pluginAuthors")
    var pluginAuthors: List<PluginAuthor>? = null

    @JsonProperty("pluginDescription")
    var pluginDescription: String? = null

    @JsonProperty("mainClassPath")
    var mainClassPath: String? = null

    var pluginInstance: PluginInstance? = null
}
