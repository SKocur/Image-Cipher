package com.imagecipher.app.controllers

import com.imagecipher.app.tools.SafeNumberParser
import com.imagecipher.app.tools.imageprocessing.map.BfsImagePainter
import com.imagecipher.app.tools.imageprocessing.map.DfsImagePainter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import javafx.fxml.FXML
import javafx.scene.control.ColorPicker
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Point
import java.io.File
import java.io.IOException

class PixelTraversalController {
    @FXML
    var animationPause: TextField? = null

    @FXML
    var iterations: TextField? = null

    @FXML
    var radioBFS: RadioButton? = null

    @FXML
    var colorPicker: ColorPicker? = null

    @FXML
    var radioDFS: RadioButton? = null

    private var preview: ImageView? = null
    private var imageGenerationThread: Thread? = null
    private var startingPoint = Point()

    private var disposable: Disposable? = null

    fun setClickObservable(observable: Observable<Point>) {
        logger.info("Setting observable")
        disposable = observable.subscribeOn(Schedulers.computation())
            .subscribe(Consumer { point: Point ->
                startingPoint = point
                runPixelTraversal()
            })
    }

    fun setPreview(preview: ImageView?) {
        this.preview = preview
    }

    fun runPixelTraversal() {
        logger.debug("Running pixel traversal from: " + startingPoint.toString())
        if (imageGenerationThread != null) {
            imageGenerationThread!!.interrupt()
        }

        if (radioBFS!!.isSelected()) {
            imageGenerationThread = Thread(Runnable {
                try {
                    BfsImagePainter.paintImage(
                        File(WindowController.fileName),
                        SafeNumberParser.getParsedNumber(iterations!!.getText()),
                        colorPicker!!.getValue(),
                        SafeNumberParser.getParsedNumber(animationPause!!.getText()),
                        preview!!,
                        startingPoint
                    )
                } catch (e: IOException) {
                    logger.error(e)
                }
            })
            imageGenerationThread!!.start()
        } else if (radioDFS!!.isSelected()) {
            imageGenerationThread = Thread(Runnable {
                try {
                    DfsImagePainter.paintImage(
                        File(WindowController.fileName),
                        SafeNumberParser.getParsedNumber(iterations!!.getText()),
                        colorPicker!!.getValue(),
                        SafeNumberParser.getParsedNumber(animationPause!!.getText()),
                        preview!!,
                        startingPoint
                    )
                } catch (e: IOException) {
                    logger.error(e)
                }
            })
            imageGenerationThread!!.start()
        }
    }

    fun dispose() {
        logger.info("Disposing")
        disposable!!.dispose()
    }

    companion object {
        private val logger: Logger = LogManager.getLogger()
    }
}
