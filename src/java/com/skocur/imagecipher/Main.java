package com.skocur.imagecipher;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows")) {
            Application.launch(Window.class, args);
        } else {

        }
    }
}
