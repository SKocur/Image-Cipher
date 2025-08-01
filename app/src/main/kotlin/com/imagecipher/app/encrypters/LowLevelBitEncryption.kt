package com.imagecipher.app.encrypters

import com.imagecipher.app.plugin.lib.annotations.IcAlgorithmSpecification
import org.apache.logging.log4j.LogManager
import java.awt.Color

@IcAlgorithmSpecification(algorithmName = "Low Level Bit Encryption")
class LowLevelBitEncryption(fileName: String) : Encrypter(fileName) {

    private var row: Int = 0
    private var col: Int = 0

    companion object {
        private val logger = LogManager.getLogger()
    }

    override fun encrypt(text: String) {
        logger.debug("Encrypting: $text")
        for (c in text.toCharArray()) {
            encryptByte(c)
        }

        // Resetting values of cursor coordinates
        col = 0
        row = 0
    }

    private fun encryptByte(character: Char) {
        for (i in 0 until 8) {
            encryptBitCharacter((character.code shr i) and 0b1)
        }
    }

    private fun encryptBitCharacter(c: Int) {
        if (col < image.width - 1) {
            val mask = 0b11111111
            val rgb = image.getRGB(col, row)
            val r = (rgb shr 16) and mask
            val g = (rgb shr 8) and mask

            var b = image.getRGB(col + 1, row) and mask
            b -= c // TODO: Handle cases where the image is too bright (b could become 0)

            val color = Color(r, g, b)
            image.setRGB(col, row, color.rgb)

            col++
        } else {
            col = 0
            row++
        }
    }
}
