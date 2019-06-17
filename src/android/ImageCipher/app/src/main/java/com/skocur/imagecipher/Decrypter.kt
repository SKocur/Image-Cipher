package com.skocur.imagecipher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color

class Decrypter(bitmap: Bitmap) {

    private var bitmap: Bitmap
    private var row: Int = 0
    private var col: Int = 0
    private var tempBitVal: Int = 0

    init {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun decryptLowLevelBits() : String {
        val sb = StringBuilder()

        while (col < bitmap.width - 1 && row < bitmap.height) {
            var c = 0

            for (i in 0..7) {
                decryptBitCharacter()

                c = c shl 1
                c = c or tempBitVal
            }

            val mask = 255
            val character: Char = (Integer.reverse(c shl 24) and mask).toChar()

            sb.append(character)
        }

        return sb.toString()
    }

    private fun decryptBitCharacter() {
        if (col < bitmap.width - 1) {
            val rgb = bitmap.getPixel(col, row)
            val b = Color.blue(rgb)

            val newB = Color.blue(bitmap.getPixel(col + 1, row))

            tempBitVal = Math.abs(newB - b)

            col += 2
        } else {
            row++
            col = 0
        }
    }
}