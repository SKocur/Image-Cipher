package com.skocur.imagecipher.controllers;

import com.skocur.imagecipher.tools.imageprocessing.ColorFilter;
import com.skocur.imagecipher.tools.imageprocessing.ImageNoise;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingController {

    @FXML
    public ImageView imageAfterPreview;
    @FXML
    public ImageView imageBeforePreview;

    @FXML
    public void initViews() {
        File file = new File(WindowController.fileName);
        Image image = new Image(file.toURI().toString());
        imageBeforePreview.setImage(image);
    }

    @FXML
    public void processRED() {
        try {
            setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName), 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void processGREEN() {
        try {
            setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName), 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void processBLUE() {
        try {
            setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName), 3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void processNOISE() {
        Runnable r = () -> {
            try {
                setProcessedImage(new ImageNoise(WindowController.fileName).createRandomNoise());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(r).start();
    }

    public void setProcessedImage(BufferedImage image) {
        imageAfterPreview.setImage(SwingFXUtils.toFXImage(image, null));
    }
}
