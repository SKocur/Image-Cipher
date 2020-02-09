package com.skocur.imagecipher.controllers;

import com.skocur.imagecipher.tools.imageprocessing.map.BfsImagePainter;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class PixelTraversalController {

    @FXML
    public TextField animationPause;

    @FXML
    public TextField iterations;

    @FXML
    public RadioButton radioBFS;

    @FXML
    public ColorPicker colorPicker;

    private ImageView preview;

    private Thread imageGenerationThread;

    void setPreview(ImageView preview) {
        this.preview = preview;
    }

    public void runPixelTraversal() {
        if (radioBFS.isSelected()) {
            if (imageGenerationThread != null) {
                imageGenerationThread.interrupt();
            }

            imageGenerationThread = new Thread(() -> {
                try {
                    BfsImagePainter.paintImage(new File(WindowController.fileName),
                            getParsedNumber(iterations.getText()),
                            colorPicker.getValue(),
                            getParsedNumber(animationPause.getText()),
                            preview);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            imageGenerationThread.start();
        }
    }

    private int getParsedNumber(String text) {
        int res;
        try {
            res = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }

        return res;
    }
}
