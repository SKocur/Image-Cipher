package com.imagecipher.app

import com.imagecipher.app.controllers.WindowController
import com.imagecipher.app.tools.UpdateChecker
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import java.io.IOException
import kotlin.system.exitProcess


object Main {
    private val logger=
        org.apache.logging.log4j.LogManager.getLogger()

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("Application launched")

        //    PluginManager.initialize(); // plugins are temporarily disabled
        val updateChecker = UpdateChecker()
        updateChecker.checkForUpdates { displayNotification() }

        if (args.isEmpty()) {
            logger.info("Opening window")
            javafx.application.Application.launch(
                WindowController::class.java,
                *args
            )
        } else {
            Main.executeCommand(args as Array<String?>)
        }
    }

    private fun executeCommand(args: Array<String?>) {
        try {
            logger.info("Launching CommandExecutor")
            CommandExecutor.executeArgs(args)
        } catch (e: IOException) {
            logger.error(e)
            exitProcess(1)
        }
    }

    private fun displayNotification(): Boolean {
        logger.info("New version is available. Displaying update alert")

        val alert = Alert(AlertType.INFORMATION)
        alert.headerText = "New update is available to download"
        alert.showAndWait()

        return true
    }
}
