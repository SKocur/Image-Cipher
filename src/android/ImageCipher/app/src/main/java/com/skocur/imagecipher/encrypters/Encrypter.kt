package com.skocur.imagecipher.encrypters

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.system.Os.mkdir
import java.nio.file.Files.exists
import android.content.ContextWrapper
import java.io.File

abstract class Encrypter(bitmap: Bitmap,
                         private var contentResolver: ContentResolver,
                         private var context: Context) {

    protected var bitmap : Bitmap

    init {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    abstract fun encrypt(text: String)

    protected fun saveEncryptedData() {
        val fileName = UUID.randomUUID().toString() + ".png"
        // TODO Saving doesn't work, no changes are seen in new image
        val url = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            fileName,
            "Image created by Image Cipher"
        )

        /*val cw = ContextWrapper(context)
        val directory = cw.getDir("Image Cipher", Context.MODE_PRIVATE)

        val path = File(directory, fileName)

        var fos: FileOutputStream?
        try {
            fos = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            Log.e("SAVE_IMAGE", e.message, e)
        }*/

        MediaScannerConnection.scanFile(context, arrayOf(url),
            arrayOf("png"), null)

        Toast.makeText(context, "Image saved to gallery in $url", Toast.LENGTH_LONG).show()
    }
}