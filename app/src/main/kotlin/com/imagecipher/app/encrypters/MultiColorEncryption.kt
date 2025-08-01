package com.imagecipher.app.encrypters

import com.imagecipher.app.plugin.lib.annotations.IcAlgorithmSpecification
import org.apache.logging.log4j.LogManager
import java.awt.Color

@IcAlgorithmSpecification(algorithmName = "Multi Color Encryption")
class MultiColorEncryption(fileName: String) : Encrypter(fileName) {

    companion object {
        private val logger = LogManager.getLogger()
    }

    override fun encrypt(text: String) {
        logger.debug("Encrypting: $text")
        var index = 0
        var hasDone = false

        for (i in 0 until image.height) {
            if (hasDone) break
            for (j in 0 until image.width) {
                if (index < text.length) {
                    val color = encryptBlue(text[index++], j, i)
                    image.setRGB(j, i, color.rgb)
                } else {
                    hasDone = true
                    break
                }
            }
        }
    }

    private fun encryptBlue(character: Char, posX: Int, posY: Int): Color {
        val argb = image.getRGB(posX, posY)

        val r = (argb shr 16) and 0b11111111
        val g = (argb shr 8) and 0b11111111

        val newB = if (character.code < 255) character.code and 0b11111111 else 0

        return Color(r, g, newB)
    }
}
