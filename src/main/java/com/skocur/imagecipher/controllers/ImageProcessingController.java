package com.skocur.imagecipher.controllers;

import com.skocur.imagecipher.Main;
import com.skocur.imagecipher.tools.imageprocessing.ColorFilter;
import com.skocur.imagecipher.tools.imageprocessing.FilteringColorMode;
import com.skocur.imagecipher.tools.imageprocessing.ImageNoise;
import io.reactivex.rxjava3.core.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageProcessingController implements Initializable {

  @FXML
  public ImageView imageAfterPreview;
  @FXML
  public ImageView imageBeforePreview;

  private PixelTraversalController pixelTraversalController;

  private Observable<Point> clickObservable;

  private static final Logger logger = LogManager.getLogger();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    File file = new File(WindowController.fileName);
    Image image = new Image(file.toURI().toString());
    imageBeforePreview.setImage(image);

    clickObservable = Observable
        .create(s -> imageAfterPreview.setOnMouseClicked((event) -> {
          if (pixelTraversalController == null) {
            return;
          }

          s.onNext(new Point((int) event.getX(), (int) event.getY()));
        }));
  }

  @FXML
  public void processRED() {
    try {
      setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName),
          FilteringColorMode.RED));
    } catch (IOException e) {
      logger.error(e);
    }
  }

  @FXML
  public void processGREEN() {
    try {
      setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName),
          FilteringColorMode.GREEN));
    } catch (IOException e) {
      logger.error(e);
    }
  }

  @FXML
  public void processBLUE() {
    try {
      setProcessedImage(ColorFilter.getColorOf(new File(WindowController.fileName),
          FilteringColorMode.BLUE));
    } catch (IOException e) {
      logger.error(e);
    }
  }

  @FXML
  public void processNOISE() {
    Runnable r = () -> {
      try {
        setProcessedImage(new ImageNoise(WindowController.fileName).createRandomNoise());
      } catch (IOException e) {
        logger.error(e);
      }
    };
    new Thread(r).start();
  }

  private void setProcessedImage(BufferedImage image) {
    imageAfterPreview.setImage(SwingFXUtils.toFXImage(image, null));
  }

  @FXML
  public void saveProcessedImage() {
    logger.info("Saving processed image");
    String filePath = WindowController.fileName;
    int extIndex = filePath.lastIndexOf('.');

    if (extIndex == -1) {
      logger.error("Invalid path. Extension not specified");
      return;
    }

    String outputPath =
        filePath.substring(0, extIndex) + "_processed" + filePath.substring(extIndex);
    File out = new File(outputPath);

    logger.debug("Path of processed image: " + out.getAbsolutePath());

    BufferedImage afterPreviewBuffer = SwingFXUtils.fromFXImage(imageAfterPreview.getImage(), null);

    try {
      ImageIO.write(afterPreviewBuffer, filePath.substring(extIndex + 1), out);
    } catch (IOException e) {
      logger.error(e);
    }
  }

  @FXML
  public void openPixelTraversing() {
    if (pixelTraversalController != null) {
      logger.warn("PixelTraversalController is already opened");
      return;
    }

    Optional<Parent> root = Optional.empty();
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          Main.class.getResource("/views/PixelTraversalWindow.fxml"));
      root = Optional.of(fxmlLoader.load());

      pixelTraversalController = fxmlLoader.getController();
      pixelTraversalController.setPreview(imageAfterPreview);
      pixelTraversalController.setClickObservable(clickObservable);
    } catch (IOException e) {
      logger.error(e);
    }

    root.ifPresent(parent -> {
      Scene scene = new Scene(parent);
      Stage stage = new Stage();
      stage.setOnCloseRequest(event -> pixelTraversalController.dispose());

      stage.setTitle("Pixel Traversal");
      stage.setScene(scene);
      stage.show();
      logger.info("Showing PixelTraversalWindow");
    });
  }
}
