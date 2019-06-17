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
        for (i in 0..7) {
            encryptBitCharacter((character.toInt() shr i) and 0b1)
        }
    }

    private fun encryptBitCharacter(c : Int) {
        if (col < bitmap.width - 1) {
            val rgb = bitmap.getPixel(col, row)
            val r = Color.red(rgb)
            val g = Color.green(rgb)

            var b = Color.blue(bitmap.getPixel(col + 1, row))
            b -= c

            //bitmap.setPixel(col, row, String.format("%02x%02x%02x", r, g, b).toInt(16))
            val color = Color.rgb(r, g, b)
            super.bitmap.setPixel(col, row, color)

            col += 2
        } else {
            row++
            col = 0
        }
    }
}