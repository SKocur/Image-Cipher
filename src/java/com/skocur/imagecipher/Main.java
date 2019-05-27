package com.skocur.imagecipher;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows") && args.length == 0) {
            Application.launch(WindowController.class, args);
        } else {
            CommandExecutor.executeArgs(args);
        }
    }
}
