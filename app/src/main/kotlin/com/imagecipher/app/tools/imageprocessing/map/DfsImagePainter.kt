package com.imagecipher.app.tools.imageprocessing.map

import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import org.apache.logging.log4j.LogManager
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.roundToInt

object DfsImagePainter {

    private val logger = LogManager.getLogger()

    /**
     * Depth First Search algorithm used to color an image.
     *
     * @param file Image file
     * @param iterations Number of iterations
     * @param penColor Color of ink that will be used to color the image
     * @param animationPause Duration of break between iterations
     * @param preview ImageView that displays live changes done by the algorithm
     * @param point Starting point for the DFS algorithm
     * @throws IOException May occur while reading the image file
     */
    @Throws(IOException::class)
    fun paintImage(
        file: File,
        iterations: Int,
        penColor: Color,
        animationPause: Int,
        preview: ImageView,
        point: Point
    ): BufferedImage {
        logger.debug("DFS painting image, $point")
        val image: BufferedImage = ImageIO.read(file)
        val imageY = image.height
        val imageX = image.width

        val scaleX = if (preview.fitWidth > 0) imageX / preview.fitWidth else 1.0
        val scaleY = if (preview.fitHeight > 0) imageY / preview.fitHeight else 1.0

        val startPointX = (point.x * scaleX).roundToInt()
        val startPointY = (point.y * scaleY).roundToInt()

        // Additional DFS logic to be implemented here

        return image
    }
}
