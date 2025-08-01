package com.imagecipher.app.rest.github

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

class Release {

    @Expose
    @SerializedName("tag_name")
    var tagName: String? = null

    @Expose
    @SerializedName("name")
    var name: String? = null

    @Expose
    @SerializedName("published_at")
    var publishedDate: Date? = null
}
