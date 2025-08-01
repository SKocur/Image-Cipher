package com.imagecipher.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import com.imagecipher.app.tools.UpdateChecker
import com.imagecipher.app.ui.App
import java.io.IOException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("Starting Image Cipher application with Compose UI")
    
     // val updateChecker = UpdateChecker()
     // updateChecker.checkForUpdates {
     //     println("New version is available")
     //     true
     // }
    
    if (args.isEmpty()) {
        println("Opening Compose window")
        
        application {
            val windowState = rememberWindowState(
                width = 1200.dp,
                height = 800.dp
            )
            
            Window(
                onCloseRequest = {
                    println("Closing application")
                    exitApplication()
                },
                state = windowState,
                title = "Image Cipher"
            ) {
                App()
            }
        }
    } else {
        executeCommand(args)
    }
}

private fun executeCommand(args: Array<String>) {
    try {
        println("Launching CommandExecutor")
        CommandExecutor.executeArgs(args.map { it }.toTypedArray())
    } catch (e: IOException) {
        e.printStackTrace()
        exitProcess(1)
    }
}
