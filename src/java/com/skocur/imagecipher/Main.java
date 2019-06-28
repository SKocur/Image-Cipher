package com.skocur.imagecipher;

import com.skocur.imagecipher.controllers.WindowController;
import javafx.application.Application;

import java.io.IOException;

public class Main {

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
