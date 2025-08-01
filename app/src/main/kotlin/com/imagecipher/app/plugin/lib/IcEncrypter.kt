package com.imagecipher.app.plugin.lib

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

abstract class IcEncrypter : AutoCloseable {
    private var fileName: String? = null
    private var image: BufferedImage? = null

    abstract fun encrypt(text: String)

    fun getFileName(): String? {
        return fileName
    }

    @Throws(IOException::class)
    fun setSourceFile(fileName: String) {
        this.fileName = fileName
        this.image = ImageIO.read(File(fileName))
    }

    fun getImage(): BufferedImage? {
        return image
    }

    fun setImage(image: BufferedImage) {
        this.image = image
    }
}
