package com.skocur.imagecipher.controllers;

import com.jfoenix.controls.JFXToggleButton;
import com.skocur.imagecipher.Decrypter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import com.skocur.imagecipher.Main;
import com.skocur.imagecipher.encrypters.*;
import java.util.ResourceBundle;

import com.skocur.imagecipher.plugin.lib.annotations.IcAlgorithmSpecification;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * WindowController This class is responsible for creating graphical user interface (GUI).
 */
public class WindowController extends Application implements Initializable {

  @FXML
  public ImageView previewImage;
  @FXML
  public Button loadImageButton;
  @FXML
  public Button imageProcessing;
  @FXML
  public Button goButton;
  @FXML
  public MenuButton cryptoAlgorithms;
  @FXML
  public JFXToggleButton modeToggle;
  @FXML
  public TextArea textHolder;

  private final String[] decryptersNames = {"Single Color Decryption",
      "Multi Color Decryption",
      "Low Level Bit Decryption"};

  public static String fileName = "";

  private static final Logger logger = LogManager.getLogger();

  @Override
  public void start(Stage myStage) {
    Optional<Parent> root = Optional.empty();
    try {
      root = Optional.of(FXMLLoader.load(
          Main.class.getResource("/views/MainWindow.fxml")
      ));
    } catch (IOException e) {
      logger.error(e);
    }

    root.ifPresent(parent -> {
      Scene scene = new Scene(parent);
      scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

      myStage.setMinWidth(900);
      myStage.setMinHeight(450);
      myStage.setTitle("Image Cipher");
      myStage.setScene(scene);
      myStage.show();

      logger.info("Showing MainWindow");
    });

    myStage.setOnCloseRequest(event -> {
      logger.info("Closing application");
      Platform.exit();
      System.exit(0);
    });
  }

  /**
   * This method takes image from file path. If file exists, buttons responsible for processing data
   * (for example encryption) will be enabled.
   */
  @FXML
  public void loadImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open image");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Image files", "*.png", "*.jpg")
    );

    logger.info("Opening file chooser dialog");
    File file = fileChooser.showOpenDialog(new Stage());

    if (file == null) {
      logger.warn("No image has been chosen");
      return;
    }

    fileName = file.getPath();
    Image image = new Image(file.toURI().toString());
    previewImage.setImage(image);

    if (file.exists()) {
      logger.info("Image file exists");
      initCryptoAlgorithms();
      goButton.setDisable(false);
      imageProcessing.setDisable(false);
    } else {
      logger.warn("Image file does not exist");
      imageProcessing.setDisable(true);
    }
  }

  @FXML
  public void launchImageProcessingWindow() {
    Optional<Parent> root = Optional.empty();
    try {
      root = Optional.of(FXMLLoader.load(
          Main.class.getResource("/views/ImageProcessingWindow.fxml")
      ));
    } catch (IOException e) {
      logger.error(e);
    }

    root.ifPresent(parent -> {
      Scene scene = new Scene(parent, 900, 500);
      Stage stage = new Stage();

      stage.setMinWidth(900);
      stage.setMinHeight(450);
      stage.setTitle("Image Processing");
      stage.setScene(scene);
      stage.show();
      logger.info("Opening ImageProcessingWindow");
    });
  }

  private void initCryptoAlgorithms() {
    cryptoAlgorithms.getItems().clear();

    ToggleGroup group = new ToggleGroup();

    if (fileName == null || fileName.equals("")) {
      logger.warn("Cannot initialize crypto algorithms, no file has been chosen");
      return;
    }

    if (modeToggle.isSelected()) {
      logger.info("Loading decryption algorithms");

      for (String name : decryptersNames) {
        RadioMenuItem radioMenuItem = new RadioMenuItem(name);
        radioMenuItem.setToggleGroup(group);
        radioMenuItem.setUserData(name);

        cryptoAlgorithms.getItems().add(radioMenuItem);
      }
    } else {
      logger.info("Loading encryption algorithms");
      for (int i = 1; i < 4; ++i) {
        Encrypter encrypter = EncrypterManager.getEncrypter(EncrypterType.getType(i), fileName);

        if (encrypter == null) {
          continue;
        }

        RadioMenuItem menuItem = getNamedMenuItem(encrypter.getClass());
        menuItem.setToggleGroup(group);
        menuItem.setUserData(encrypter);

        cryptoAlgorithms.getItems().add(menuItem);
      }
    }
  }

  @NotNull
  private RadioMenuItem getNamedMenuItem(@NotNull Class<?> algorithm) {
    IcAlgorithmSpecification[] specifications = algorithm
        .getDeclaredAnnotationsByType(IcAlgorithmSpecification.class);

    RadioMenuItem menuItem;
    if (specifications.length > 0) {
      menuItem = new RadioMenuItem(specifications[0].algorithmName());
    } else {
      logger.warn(String.format("%s class does not specified its name", algorithm.getName()));
      menuItem = new RadioMenuItem(getNameFromClass(algorithm));
    }

    return menuItem;
  }

  @NotNull
  private String getNameFromClass(@NotNull Class<?> packageName) {
    String[] parts = packageName.getName().split("\\.");
    return parts[parts.length - 1];
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    modeToggle.selectedProperty().addListener(((observable, oldValue, newValue) -> {
      initCryptoAlgorithms();
      modeToggle.setText(newValue ? "Decryption enabled" : "Encryption enabled");
    }));
  }

  @FXML
  public void executeAlgorithm() {
    logger.info("Executing algorithm");
    if (isDecryptionSelected()) {
      executeDecryption();
    } else {
      executeEncryption();
    }
  }

  private boolean isDecryptionSelected() {
    return modeToggle.isSelected();
  }

  private void executeEncryption() {
    logger.info("Executing encryption");
    MenuItem menuItem = cryptoAlgorithms.getItems().get(0);

    if (!(menuItem instanceof RadioMenuItem)) {
      logger.error("Menu item is not instance of RadioMenuItem");
      return;
    }

    RadioMenuItem radioMenuItem = (RadioMenuItem) menuItem;
    Toggle selectedToggle = radioMenuItem.getToggleGroup().getSelectedToggle();

    if (selectedToggle == null) {
      logger.warn("No algorithm has been chosen");
      return;
    }

    Object object = selectedToggle.getUserData();

    if (!(object instanceof Encrypter)) {
      logger.error("Selected algorithm is not instance of Encrypter");
      return;
    }

    try (Encrypter encrypter = (Encrypter) selectedToggle.getUserData()) {
      encrypter.encrypt(textHolder.getText());
    }
  }

  private void executeDecryption() {
    logger.info("Executing decryption");
    MenuItem menuItem = cryptoAlgorithms.getItems().get(0);

    if (!(menuItem instanceof RadioMenuItem radioMenuItem)) {
      logger.error("Menu item is not instance of RadioMenuItem");
      return;
    }

    Toggle selectedToggle = radioMenuItem.getToggleGroup().getSelectedToggle();

    if (selectedToggle == null) {
      logger.warn("No algorithm has been chosen");
      return;
    }

    Object object = selectedToggle.getUserData();

    if (!(object instanceof String name)) {
      logger.error("Data od decryption algorithm is not a String instance");
      return;
    }

    try {
      logger.info("Chosen decryption algorithm: " + name);
      String message = "";
      if (name.equals(decryptersNames[0])) {
        message = Decrypter.decrypt(fileName);
      } else if (name.equals(decryptersNames[1])) {
        message = Decrypter.decryptBlue(fileName);
      } else if (name.equals(decryptersNames[2])) {
        Decrypter decrypter = new Decrypter(fileName);
        message = decrypter.decryptLowLevelBits();
      }

      textHolder.setText(message);
    } catch (IOException e) {
      logger.error(e);
    }
  }
}
