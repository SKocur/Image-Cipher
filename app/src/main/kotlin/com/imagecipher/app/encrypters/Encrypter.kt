package com.imagecipher.app.encrypters

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Abstract class representing encrypters within Image Cipher. Every encrypter operates only on pixels of
 * a loaded image. Output is always saved as soon as encryption is done. Getter methods that return
 * dimensions of the image can be used by outside classes to calculate the capacity of data that are allowed
 * to be stored.
 */
abstract class Encrypter(protected var fileName: String) : AutoCloseable {

    protected var image: BufferedImage = ImageIO.read(File(fileName))
    private val imageWidth: Int = image.width

    /**
     * Abstract method for encrypting text.
     *
     * @param text Text to be encrypted
     */
    abstract fun encrypt(text: String)

    /**
     * Method that should be invoked after all encryption processes have finished.
     */
    override fun close() {
        try {
            val file = File(fileName)
            ImageIO.write(image, "png", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getImageWidth(): Int {
        return imageWidth
    }
}
