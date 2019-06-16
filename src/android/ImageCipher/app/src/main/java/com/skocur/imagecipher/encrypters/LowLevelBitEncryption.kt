package com.skocur.imagecipher.encrypters

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log

class LowLevelBitEncryption(bitmap: Bitmap,
                            contentResolver: ContentResolver,
                            context: Context)
    : Encrypter(bitmap, contentResolver, context) {

    private var row : Int = 0
    private var col : Int = 0

    override fun encrypt(text: String) {
        text.toCharArray().forEach {
            encryptByte(it)
        }

        col = 0
        row = 0

        saveEncryptedData()
    }

    private fun encryptByte(character : Char) {
        for (i in 0..8) {
            encryptBitCharacter((character.toInt() shr i) and 0b1)
        }
    }

    private fun encryptBitCharacter(c : Int) {
        if (col < bitmap.width - 1) {
            val mask = 255
            val rgb = bitmap.getPixel(col, row)
            val r = (rgb shr 16) and mask
            val g = (rgb shr 8) and mask

            var b = bitmap.getPixel(col + 1, row) and mask
            b -= c

            bitmap.setPixel(col, row, String.format("%02x%02x%02x", r, g, b).toInt(16))
        }
    }
}