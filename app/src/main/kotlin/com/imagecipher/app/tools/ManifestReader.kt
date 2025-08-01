package com.imagecipher.app.tools

import com.jcabi.manifests.Manifests
import org.apache.logging.log4j.LogManager
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object ManifestReader {

    private val logger = LogManager.getLogger()

    fun getBuildTime(): Date? {
        logger.info("Getting build time")
        val dateFormat: DateFormat = SimpleDateFormat("yyyy.MM.dd")
        return try {
            val time = Manifests.read("Build-Time")
            if (time.isNullOrEmpty()) {
                logger.warn("Build-Time is empty in MANIFEST")
                null
            } else {
                dateFormat.parse(time)
            }
        } catch (e: ParseException) {
            logger.error(e)
            null
        }
    }
}
