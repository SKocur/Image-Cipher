package com.skocur.imagecipher.controllers;

import com.skocur.imagecipher.tools.imageprocessing.map.BfsImagePainter;
import com.skocur.imagecipher.tools.imageprocessing.map.DfsImagePainter;
import com.skocur.imagecipher.tools.SaveNumberParser;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
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

  @FXML
  public ColorPicker colorPicker;

  @FXML
  public RadioButton radioDFS;

  private ImageView preview;
  private Thread imageGenerationThread;
  private Point startingPoint = new Point();

  private Disposable disposable;

  void setClickObservable(Observable<Point> observable) {
    disposable = observable.subscribeOn(Schedulers.computation())
        .subscribe(point -> {
          startingPoint = point;
          runPixelTraversal();
        });
  }

  void setPreview(ImageView preview) {
    this.preview = preview;
  }

  public void runPixelTraversal() {
    if (imageGenerationThread != null) {
      imageGenerationThread.interrupt();
    }

    if (radioBFS.isSelected()) {
      imageGenerationThread = new Thread(() -> {
        try {
          BfsImagePainter.paintImage(new File(WindowController.fileName),
              SaveNumberParser.getParsedNumber(iterations.getText()),
              colorPicker.getValue(),
              SaveNumberParser.getParsedNumber(animationPause.getText()),
              preview,
              startingPoint);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      imageGenerationThread.start();
    } else if (radioDFS.isSelected()) {
      imageGenerationThread = new Thread(() -> {
        try {
          DfsImagePainter.paintImage(new File(WindowController.fileName),
              SaveNumberParser.getParsedNumber(iterations.getText()),
              colorPicker.getValue(),
              SaveNumberParser.getParsedNumber(animationPause.getText()),
              preview,
              startingPoint);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      imageGenerationThread.start();
    }
  }

  void dispose() {
    disposable.dispose();
  }
}
