package com.skocur.imagecipher.controllers;

import com.skocur.imagecipher.tools.imageprocessing.map.BfsImagePainter;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PixelTraversalController {

    @FXML
    public TextField animationPause;

    @FXML
    public TextField iterations;

    @FXML
    public RadioButton radioBFS;

    private ImageView preview;

    void setPreview(ImageView preview) {
        this.preview = preview;
    }

    public void runPixelTraversal() {
        if (radioBFS.isSelected()) {
            new Thread(() -> {
                try {
                    BfsImagePainter.paintImage(new File(WindowController.fileName),
                            getParsedNumber(iterations.getText()),
                            new Color(255, 0, 0),
                            getParsedNumber(animationPause.getText()),
                            preview);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private int getParsedNumber(String text) {
        int res;
        try {
            res = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }

        return res;
    }
}
