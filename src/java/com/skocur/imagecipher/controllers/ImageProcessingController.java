package com.skocur.imagecipher.controllers;

import com.skocur.imagecipher.tools.imageprocessing.ColorFilter;
import com.skocur.imagecipher.tools.imageprocessing.FilteringColorMode;
import com.skocur.imagecipher.tools.imageprocessing.ImageNoise;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingController {

    @FXML
    public ImageView imageAfterPreview;
    @FXML
    public ImageView imageBeforePreview;

    private BufferedImage bufferedImage;

    @FXML
    public void initViews() {
        File file = new File(WindowController.fileName);
        Image image = new Image(file.toURI().toString());
        imageBeforePreview.setImage(image);
    }

    @FXML
    public void processRED() {
        try {
            setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName),
                    FilteringColorMode.RED));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void processGREEN() {
        try {
            setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName),
                    FilteringColorMode.GREEN));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void processBLUE() {
        try {
            setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName),
                    FilteringColorMode.BLUE));
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
        bufferedImage = image;
    }

    @FXML
    public void saveProcessedImage() {
        String filePath = WindowController.fileName;
        int extIndex = filePath.lastIndexOf('.');

        if (extIndex == -1) {
            throw new Error("Invalid path. Extension not specified");
        }

        String outputPath = filePath.substring(0, extIndex) + "_processed" + filePath.substring(extIndex);
        File out = new File(outputPath);

        try {
            ImageIO.write(this.bufferedImage, filePath.substring(extIndex + 1), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
