package com.imagecipher.app.tools

import com.jcabi.manifests.Manifests
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object ManifestReader {

    fun getBuildTime(): Date? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy.MM.dd")
        return try {
            val time = Manifests.read("Build-Time")
            if (time.isNullOrEmpty()) {
                null
            } else {
                dateFormat.parse(time)
            }
        } catch (e: ParseException) {
            null
        } catch (e: Exception) {
            null
        }
    }
}
