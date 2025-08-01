package com.imagecipher.app.encrypters

import com.imagecipher.app.Config
import com.imagecipher.app.plugin.lib.annotations.IcAlgorithmSpecification
import org.apache.logging.log4j.LogManager
import java.awt.Color

@IcAlgorithmSpecification(algorithmName = "Single Color Encryption")
class SingleColorEncryption(fileName: String) : Encrypter(fileName) {

    private val asciiChars: MutableList<Int> = mutableListOf()

    companion object {
        private val logger = LogManager.getLogger()
    }

    override fun encrypt(text: String) {
        logger.debug("Encrypting: $text")
        for (c in text) {
            asciiChars.add(c.code)
        }

        var index = 0
        for (i in 0 until getImageWidth()) {
            if (i % Config.SPACING_CIPHER == 0 && index < asciiChars.size) {
                val greenShade = Color(1, asciiChars[index], 1)
                image.setRGB(i, Config.IMAGE_MARGIN_TOP, greenShade.rgb)
                index++
            }
        }
    }
}
