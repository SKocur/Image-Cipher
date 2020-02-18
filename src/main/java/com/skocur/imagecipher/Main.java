package com.skocur.imagecipher;

import com.skocur.imagecipher.controllers.WindowController;
import javafx.application.Application;

import java.io.IOException;

/**
 * "Starter" class responsible for choosing whether open JavaFX window or not.
 */
public class Main {

    /**
     * Opens JavaFX window when no parameters are passed. In other case
     * it invokes method responsible for interpreting and executing
     * arguments.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Application.launch(WindowController.class, args);
        } else {
            executeCommand(args);
        }
    }

    private static void executeCommand(String[] args){
        try {
            CommandExecutor.executeArgs(args);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
