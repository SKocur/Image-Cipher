package com.imagecipher.app

import com.imagecipher.app.Config.SPACING_CIPHER
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.security.interfaces.RSAPrivateKey
import javax.crypto.Cipher
import javax.imageio.ImageIO
import kotlin.math.abs

class Decrypter(fileName: String) {
    private var row = 0
    private var col = 0
    private var tempBitVal = 0
    private val originalImage: BufferedImage = ImageIO.read(File(fileName))

    fun decryptLowLevelBits(): String {
        val stringBuilder = java.lang.StringBuilder()

        while (col < originalImage.getWidth() - 1 && row < originalImage.getHeight()) {
            var c = 0

            for (i in 0..7) {
                decryptBitCharacter()

                c = c shl 1
                c = c or tempBitVal
            }

            val charC = (Integer.reverse(c shl 24) and 255).toChar()

            stringBuilder.append(charC)
        }

        return stringBuilder.toString()
    }

    private fun decryptBitCharacter() {
        if (col < originalImage.getWidth() - 1) {
            val mask = 255
            val rgb = originalImage.getRGB(col, row)

            val b = rgb and mask

            val newB = originalImage.getRGB(col + 1, row) and mask

            tempBitVal = abs(newB - b)

            col += 2
        } else {
            row++
            col = 0
        }
    }

    @Throws(Exception::class)
    fun RSADecryption(
        text: String,
        key: RSAPrivateKey
    ): String {
        val bytes: ByteArray = text.toByteArray()
        val decrypter = Cipher.getInstance("RSA")
        decrypter.init(Cipher.DECRYPT_MODE, key)
        return String(decrypter.doFinal(bytes))
    }

    companion object {
        /**
         * This method decrypt text message from given image. It reads level of shadow on green channel
         * from pixel.
         *
         * @param fileName Name of the image
         * @return text Decrypted text
         * @throws IOException When file cannot be found.
         * @see Config
         */
        @Throws(IOException::class)
        fun decrypt(fileName: String): String {
            val bufferedImage = ImageIO.read(File(fileName))

            val width = bufferedImage.getWidth()
            val stringBuilder = StringBuilder()

            for (pixel in 0..<width) {
                if (pixel % SPACING_CIPHER == 0) {
                    val colorShade = Color(
                        bufferedImage.getRGB(
                            pixel,
                            Config.IMAGE_MARGIN_TOP
                        ), true
                    )
                    stringBuilder.append(colorShade.getGreen().toChar())
                }
            }

            return stringBuilder.toString()
        }

        @Throws(IOException::class)
        fun decryptBlue(fileName: String): String {
            val bufferedImage = ImageIO.read(File(fileName))

            val stringBuilder = StringBuilder()

            for (i in 0..<bufferedImage.getHeight()) {
                for (j in 0..<bufferedImage.getWidth()) {
                    val b = bufferedImage.getRGB(j, i) and 255

                    stringBuilder.append(b.toChar())
                }
            }

            return stringBuilder.toString()
        }
    }
}
