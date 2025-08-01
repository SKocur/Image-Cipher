package com.imagecipher.app.controllers

import com.imagecipher.app.Decrypter
import com.imagecipher.app.Main
import com.imagecipher.app.encrypters.Encrypter
import com.imagecipher.app.encrypters.EncrypterManager
import com.imagecipher.app.encrypters.EncrypterType
import com.imagecipher.app.plugin.lib.annotations.IcAlgorithmSpecification
import com.jfoenix.controls.JFXToggleButton
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.net.URL
import java.util.*

class WindowController : Application(), Initializable {
    @FXML
    var previewImage: ImageView? = null

    @FXML
    var loadImageButton: Button? = null

    @FXML
    var imageProcessing: Button? = null

    @FXML
    var goButton: Button? = null

    @FXML
    var cryptoAlgorithms: MenuButton? = null

    @FXML
    var modeToggle: JFXToggleButton? = null

    @FXML
    var textHolder: TextArea? = null

    private val decryptersNames = arrayOf<String?>(
        "Single Color Decryption",
        "Multi Color Decryption",
        "Low Level Bit Decryption"
    )

    override fun start(myStage: Stage) {
        val root = FXMLLoader.load<Parent?>(
            Main::class.java.getResource("/views/MainWindow.fxml")
        )

        root?.let { parent: Parent? ->
            val scene = Scene(parent)
            scene.stylesheets.add("org/kordamp/bootstrapfx/bootstrapfx.css")

            myStage.setMinWidth(900.0)
            myStage.setMinHeight(450.0)
            myStage.setTitle("Image Cipher")
            myStage.setScene(scene)
            myStage.show()
            logger.info("Showing MainWindow")
        }

        myStage.onCloseRequest = EventHandler { event: WindowEvent? ->
            logger.info("Closing application")
            Platform.exit()
            System.exit(0)
        }
    }

    /**
     * This method takes image from file path. If file exists, buttons responsible for processing data
     * (for example encryption) will be enabled.
     */
    @FXML
    fun loadImage() {
        val fileChooser = FileChooser()
        fileChooser.title = "Open image"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg")
        )

        logger.info("Opening file chooser dialog")
        val file = fileChooser.showOpenDialog(Stage())

        if (file == null) {
            logger.warn("No image has been chosen")
            return
        }

        fileName = file.getPath()
        val image = Image(file.toURI().toString())
        previewImage!!.image = image

        if (file.exists()) {
            logger.info("Image file exists")
            initCryptoAlgorithms()
            goButton!!.setDisable(false)
            imageProcessing!!.setDisable(false)
        } else {
            logger.warn("Image file does not exist")
            imageProcessing!!.setDisable(true)
        }
    }

    @FXML
    fun launchImageProcessingWindow() {
        val root = FXMLLoader.load<Parent?>(
            Main::class.java.getResource("/views/ImageProcessingWindow.fxml")
        )

        root?.let { parent: Parent? ->
            val scene = Scene(parent, 900.0, 500.0)
            val stage = Stage()

            stage.minWidth = 900.0
            stage.minHeight = 450.0
            stage.title = "Image Processing"
            stage.scene = scene
            stage.show()
            logger.info("Opening ImageProcessingWindow")
        }
    }

    private fun initCryptoAlgorithms() {
        cryptoAlgorithms!!.items.clear()

        val group = ToggleGroup()

        if (fileName == "") {
            logger.warn("Cannot initialize crypto algorithms, no file has been chosen")
            return
        }

        if (modeToggle!!.isSelected()) {
            logger.info("Loading decryption algorithms")

            for (name in decryptersNames) {
                val radioMenuItem = RadioMenuItem(name)
                radioMenuItem.toggleGroup = group
                radioMenuItem.userData = name

                cryptoAlgorithms!!.items.add(radioMenuItem)
            }
        } else {
            logger.info("Loading encryption algorithms")
            for (i in 1..3) {
                val encrypter: Encrypter? =
                    EncrypterManager.getEncrypter(EncrypterType.getType(i), checkNotNull(fileName))

                if (encrypter == null) {
                    continue
                }

                val menuItem = getNamedMenuItem(encrypter.javaClass)
                menuItem.toggleGroup = group
                menuItem.userData = encrypter

                cryptoAlgorithms!!.getItems().add(menuItem)
            }
        }
    }

    private fun getNamedMenuItem(algorithm: Class<*>): RadioMenuItem {
        val specifications: Array<IcAlgorithmSpecification?> = algorithm
            .getDeclaredAnnotationsByType(IcAlgorithmSpecification::class.java)

        val menuItem: RadioMenuItem
        if (specifications.isNotEmpty()) {
            menuItem = RadioMenuItem(specifications[0]!!.algorithmName)
        } else {
            logger.warn(String.format("%s class does not specified its name", algorithm.getName()))
            menuItem = RadioMenuItem(getNameFromClass(algorithm))
        }

        return menuItem
    }

    private fun getNameFromClass(packageName: Class<*>): String {
        val parts = packageName.getName().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return parts[parts.size - 1]
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        modeToggle!!.selectedProperty()
            .addListener((ChangeListener { observable: ObservableValue<out Boolean?>?, oldValue: Boolean?, newValue: Boolean? ->
                initCryptoAlgorithms()
                modeToggle!!.setText(if (newValue == true) "Decryption enabled" else "Encryption enabled")
            }))
    }

    @FXML
    fun executeAlgorithm() {
        logger.info("Executing algorithm")
        if (this.isDecryptionSelected) {
            executeDecryption()
        } else {
            executeEncryption()
        }
    }

    private val isDecryptionSelected: Boolean
        get() = modeToggle!!.isSelected()

    private fun executeEncryption() {
        logger.info("Executing encryption")
        val menuItem = cryptoAlgorithms!!.getItems().get(0)

        if (menuItem !is RadioMenuItem) {
            logger.error("Menu item is not instance of RadioMenuItem")
            return
        }

        val radioMenuItem = menuItem
        val selectedToggle = radioMenuItem.getToggleGroup().getSelectedToggle()

        if (selectedToggle == null) {
            logger.warn("No algorithm has been chosen")
            return
        }

        val `object` = selectedToggle.userData

        if (`object` !is Encrypter) {
            logger.error("Selected algorithm is not instance of Encrypter")
            return
        }

        (selectedToggle.userData as Encrypter).use { encrypter ->
            encrypter.encrypt(textHolder!!.getText())
        }
    }

    private fun executeDecryption() {
        logger.info("Executing decryption")
        val menuItem = cryptoAlgorithms!!.getItems().get(0)

        if (menuItem !is RadioMenuItem) {
            logger.error("Menu item is not instance of RadioMenuItem")
            return
        }

        val selectedToggle = menuItem.getToggleGroup().getSelectedToggle()

        if (selectedToggle == null) {
            logger.warn("No algorithm has been chosen")
            return
        }

        val `object` = selectedToggle.userData

        if (`object` !is String) {
            logger.error("Data od decryption algorithm is not a String instance")
            return
        }

        try {
            logger.info("Chosen decryption algorithm: " + `object`)
            val fileName = checkNotNull(fileName)
            var message: String? = ""
            if (`object` == decryptersNames[0]) {
                message = Decrypter.decrypt(fileName)
            } else if (`object` == decryptersNames[1]) {
                message = Decrypter.decryptBlue(fileName)
            } else if (`object` == decryptersNames[2]) {
                val decrypter = Decrypter(fileName)
                message = decrypter.decryptLowLevelBits()
            }

            textHolder!!.setText(message)
        } catch (e: IOException) {
            logger.error(e)
        }
    }

    companion object {
        var fileName: String = ""

        private val logger: Logger = LogManager.getLogger()
    }
}
