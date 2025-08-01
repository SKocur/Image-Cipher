package com.imagecipher.app.controllers

import com.imagecipher.app.Main
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import javafx.embed.swing.SwingFXUtils
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class ImageProcessingController : Initializable {
    @FXML
    var imageAfterPreview: ImageView? = null

    @FXML
    var imageBeforePreview: ImageView? = null

    private var pixelTraversalController: PixelTraversalController? = null

    private var clickObservable: Observable<Point>? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val file = File(WindowController.fileName)
        val image = Image(file.toURI().toString())
        imageBeforePreview!!.image = image

        clickObservable = Observable
            .create<Point> { s: ObservableEmitter<Point> ->
                imageAfterPreview!!.setOnMouseClicked(
                    EventHandler { event: MouseEvent ->
                        if (pixelTraversalController == null) {
                            return@EventHandler
                        }
                        s.onNext(Point(event.x.toInt(), event.y.toInt()))
                    })
            }
    }

    @FXML
    fun saveProcessedImage() {
        logger.info("Saving processed image")
        val filePath: String = WindowController.fileName
        val extIndex = filePath.lastIndexOf('.')

        if (extIndex == -1) {
            logger.error("Invalid path. Extension not specified")
            return
        }

        val outputPath =
            filePath.substring(0, extIndex) + "_processed" + filePath.substring(extIndex)
        val out = File(outputPath)

        logger.debug("Path of processed image: " + out.getAbsolutePath())

        val afterPreviewBuffer: BufferedImage =
            SwingFXUtils.fromFXImage(imageAfterPreview!!.getImage(), null)

        try {
            ImageIO.write(afterPreviewBuffer, filePath.substring(extIndex + 1), out)
        } catch (e: IOException) {
            logger.error(e)
        }
    }

    @FXML
    fun openPixelTraversing() {
        if (pixelTraversalController != null) {
            logger.warn("PixelTraversalController is already opened")
            return
        }

        val fxmlLoader = FXMLLoader(
            Main::class.java.getResource("/views/PixelTraversalWindow.fxml")
        )

        val root = fxmlLoader.load<Parent?>()

        pixelTraversalController = fxmlLoader.getController<PixelTraversalController?>()
        pixelTraversalController!!.setPreview(imageAfterPreview)
        pixelTraversalController!!.setClickObservable(clickObservable!!)

        root?.let { parent: Parent? ->
            val scene = Scene(parent)
            val stage = Stage()
            stage.onCloseRequest =
                EventHandler { event: WindowEvent? -> pixelTraversalController!!.dispose() }

            stage.title = "Pixel Traversal"
            stage.scene = scene
            stage.show()
            logger.info("Showing PixelTraversalWindow")
        }
    }

    companion object {
        private val logger: Logger = LogManager.getLogger()
    }
}
