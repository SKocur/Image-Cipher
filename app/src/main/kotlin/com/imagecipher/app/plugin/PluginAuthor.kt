package com.imagecipher.app.plugin

import com.fasterxml.jackson.annotation.JsonProperty

class PluginAuthor {

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty("email")
    var email: String? = null
}
