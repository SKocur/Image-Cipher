package com.skocur.imagecipher.controllers;

import com.imagecipher.icsdk.annotations.IcAlgorithmSpecification;
import com.jfoenix.controls.JFXToggleButton;
import com.skocur.imagecipher.Decrypter;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.util.Optional;

import com.skocur.imagecipher.Main;
import com.skocur.imagecipher.encrypters.*;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

/**
 * WindowController This class is responsible for creating graphical user interface (GUI).
 *
 * @author Szymon Kocur
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

  private String[] decryptersNames = {"Single Color Decryption",
      "Multi Color Decryption",
      "Low Level Bit Decryption"};

  // Default option is Low Level Bit Encryption/Decryption
  private int cryptoOption = 3;
  private RSAPrivateKey key; //I can't find any better way to save this key,saving every private key in it's own file would kind of defeat the purpose of having one
  public static String fileName = "";

  @Override
  public void start(Stage myStage) {
    Optional<Parent> root = Optional.empty();
    try {
      root = Optional.of(FXMLLoader.load(
          Main.class.getResource("/views/MainWindow.fxml")
      ));
    } catch (IOException e) {
      e.printStackTrace();
    }

    root.ifPresent(parent -> {
      Scene scene = new Scene(parent);
//      scene.getStylesheets().add(
//          Main.class.getResource("/views/styles/style_main_window.css").toString()
//      );
      scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

      myStage.setMinWidth(900);
      myStage.setMinHeight(450);
      myStage.setTitle("Image Cipher");
      myStage.setScene(scene);
      myStage.show();
    });
  }

//  private void initCryptoMode() {
//    int size = decryptionMode.getItems().size();
//
//    if (size == encryptionMode.getItems().size()) {
//      for (int i = 0; i < size; i++) {
//        int finalI = i;
//        decryptionMode.getItems().get(i).setOnAction(e ->
//            cryptoOption = finalI + 1
//        );
//
//        encryptionMode.getItems().get(i).setOnAction(e ->
//            cryptoOption = finalI + 1
//        );
//      }
//
//      decryptButton.setDisable(false);
//      encryptButton.setDisable(false);
//    }
//  }

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

    File file = fileChooser.showOpenDialog(new Stage());

    if (file == null) {
      return;
    }

    fileName = file.getPath();
    Image image = new Image(file.toURI().toString());
    previewImage.setImage(image);

    if (file.exists()) {
      initCryptoAlgorithms();
      goButton.setDisable(false);
      imageProcessing.setDisable(false);
    } else {
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
      e.printStackTrace();
    }

    root.ifPresent(parent -> {
      Scene scene = new Scene(parent, 900, 500);
      Stage stage = new Stage();

      stage.setMinWidth(900);
      stage.setMinHeight(450);
      stage.setTitle("Image Processing");
      stage.setScene(scene);
      stage.show();
    });
  }

  private void initCryptoAlgorithms() {
    cryptoAlgorithms.getItems().clear();

    ToggleGroup group = new ToggleGroup();

    if (modeToggle.isSelected()) {
      for (String name : decryptersNames) {
        RadioMenuItem radioMenuItem = new RadioMenuItem(name);
        radioMenuItem.setToggleGroup(group);
        radioMenuItem.setUserData(name);

        cryptoAlgorithms.getItems().add(radioMenuItem);
      }
    } else {
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
    MenuItem menuItem = cryptoAlgorithms.getItems().get(0);

    if (!(menuItem instanceof RadioMenuItem)) {
      System.err.println("Menu item is not instance of RadioMenuItem");
      return;
    }

    RadioMenuItem radioMenuItem = (RadioMenuItem) menuItem;
    Toggle selectedToggle = radioMenuItem.getToggleGroup().getSelectedToggle();

    if (selectedToggle == null) {
      System.err.println("No algorithm has been chosen");
      return;
    }

    Object object = selectedToggle.getUserData();

    if (!(object instanceof Encrypter)) {
      System.err.println("Selected algorithm is not instance of Encrypter");
      return;
    }

    try (Encrypter encrypter = (Encrypter) selectedToggle.getUserData()) {
      encrypter.encrypt(textHolder.getText());
    }
  }

  private void executeDecryption() {
    MenuItem menuItem = cryptoAlgorithms.getItems().get(0);

    if (!(menuItem instanceof RadioMenuItem)) {
      System.err.println("Menu item is not instance of RadioMenuItem");
      return;
    }

    RadioMenuItem radioMenuItem = (RadioMenuItem) menuItem;
    Toggle selectedToggle = radioMenuItem.getToggleGroup().getSelectedToggle();

    if (selectedToggle == null) {
      System.err.println("No algorithm has been chosen");
      return;
    }

    Object object = selectedToggle.getUserData();

    if (!(object instanceof String)) {
      System.err.println("Data od decryption algorithm is not a String instance");
      return;
    }

    try {
      String name = (String) object;
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
      System.err.println(e.getMessage());
    }
  }
}
