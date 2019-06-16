package com.skocur.imagecipher.encrypters

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import java.util.*

abstract class Encrypter {

    protected var bitmap : Bitmap
    private var contentResolver : ContentResolver
    private var context: Context

    constructor(bitmap: Bitmap, contentResolver: ContentResolver, context: Context) {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        this.contentResolver = contentResolver
        this.context = context
    }

    abstract fun encrypt(text: String)

    protected fun saveEncryptedData() {
        MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            UUID.randomUUID().toString(),
            "Image creates by Image Cipher"
        )

        Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_LONG).show()
    }
}