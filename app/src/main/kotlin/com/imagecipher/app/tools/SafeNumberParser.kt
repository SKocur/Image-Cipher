package com.imagecipher.app.tools

import org.apache.logging.log4j.LogManager

object SafeNumberParser {

    private val logger = LogManager.getLogger()

    fun getParsedNumber(text: String?): Int {
        logger.debug("Parsing number: $text")
        return try {
            text?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            logger.warn("Invalid number format, returning 0")
            0
        }
    }
}
