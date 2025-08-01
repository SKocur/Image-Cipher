package com.imagecipher.app.tools

import javafx.scene.paint.Color
import kotlin.math.roundToInt

object ColorParser {

    fun getColor(color: Color): Int {
        var R = (255 * color.red).roundToInt()
        var G = (255 * color.green).roundToInt()
        var B = (255 * color.blue).roundToInt()

        R = (R shl 16) and 0x00FF0000
        G = (G shl 8) and 0x0000FF00
        B = B and 0x000000FF

        return 0xFF000000.toInt() or R or G or B
    }
}
