package com.skocur.imagecipher;

import com.skocur.imagecipher.controllers.WindowController;
import com.skocur.imagecipher.plugin.PluginManager;
import com.skocur.imagecipher.tools.UpdateChecker;
import javafx.application.Application;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static Logger logger = LogManager.getLogger();

  public static void main(String[] args) {
    logger.info("Application launched");
//    PluginManager.initialize(); // plugins are temporarily disabled

    UpdateChecker updateChecker = new UpdateChecker();
    updateChecker.checkForUpdates(Main::displayNotification);

    if (args.length == 0) {
      logger.info("Opening window");
      Application.launch(WindowController.class, args);
    } else {
      executeCommand(args);
    }
  }

  private static void executeCommand(String[] args) {
    try {
      logger.info("Launching CommandExecutor");
      CommandExecutor.executeArgs(args);
    } catch (IOException e) {
      logger.error(e);
      System.exit(1);
    }
  }

  private static boolean displayNotification() {
    logger.info("New version is available. Displaying update alert");

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setHeaderText("New update is available to download");
    alert.showAndWait();

    return true;
  }
}
