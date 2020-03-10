package com.skocur.imagecipher.controllers;

import com.imagecipher.icsdk.annotations.IcAlgorithmSpecification;
import com.jfoenix.controls.JFXToggleButton;
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
  public Button encryptButton;
  @FXML
  public TextArea textToEncrypt;
  @FXML
  public MenuButton encryptionMode;
  @FXML
  public MenuButton decryptionMode;
  @FXML
  public Button decryptButton;
  @FXML
  public Text messageFromImage;
  @FXML
  public Button imageProcessing;
  @FXML
  public Button goButton;
  @FXML
  public MenuButton cryptoAlgorithms;
  @FXML
  public JFXToggleButton modeToggle;

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

//    fileName = imagePathTextField.getText();
    File file = fileChooser.showOpenDialog(new Stage());

    if (file == null) {
      return;
    }

    fileName = file.getPath();
    Image image = new Image(file.toURI().toString());
    previewImage.setImage(image);

    if (file.exists()) {
//      initCryptoMode();
      initCryptoAlgorithms();
      goButton.setDisable(false);
      imageProcessing.setDisable(false);
    } else {
      encryptButton.setDisable(true);
      decryptButton.setDisable(true);
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

    if (modeToggle.isSelected()) {
      //TODO add decrypters
    } else {
      for (int i = 1; i < 4; ++i) {
        Encrypter encrypter = EncrypterManager.getEncrypter(EncrypterType.getType(i), fileName);

        if (encrypter == null) {
          continue;
        }

        MenuItem menuItem = getNamedMenuItem(encrypter.getClass());

        menuItem.setUserData(encrypter);
        cryptoAlgorithms.getItems().add(menuItem);
      }
    }
  }

  @NotNull
  private MenuItem getNamedMenuItem(@NotNull Class<?> algorithm) {
    IcAlgorithmSpecification[] specifications = algorithm
        .getDeclaredAnnotationsByType(IcAlgorithmSpecification.class);

    MenuItem menuItem;
    if (specifications.length > 0) {
      menuItem = new MenuItem(specifications[0].algorithmName());
    } else {
      menuItem = new MenuItem(getNameFromClass(algorithm));
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

//  @FXML
//  public void encrypt() {
//    try (Encrypter encrypter = EncrypterManager.getEncrypter(
//        EncrypterType.getType(cryptoOption), imagePathTextField.getText()
//    )) {
//
//      if (encrypter == null) {
//        return;
//      }
//
//      encrypter.encrypt(textToEncrypt.getText());
//      if (encrypter instanceof RSAEncryption) {
//        RSAEncryption r = (RSAEncryption) encrypter;
//        key = r.getPrivKey();
//      }
//    }
//  }

//  @FXML
//  public void decrypt() throws IOException {
//    String message = "";
//    Decrypter decrypter;
//    switch (cryptoOption) {
//      case 1:
//        message = Decrypter.decrypt(imagePathTextField.getText());
//        break;
//      case 2:
//        message = Decrypter.decryptBlue(imagePathTextField.getText());
//        break;
//      case 3:
//        decrypter = new Decrypter(imagePathTextField.getText());
//        message = decrypter.decryptLowLevelBits();
//        break;
//      case 4:
//        decrypter = new Decrypter(imagePathTextField.getText());
//        message = decrypter.decryptLowLevelBits();
//        try {
//          message = decrypter.RSADecryption(message, key);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//        break;
//      default:
//        System.err.println("No valid decryption mode was chosen!");
//        System.exit(2);
//    }
//
//    messageFromImage.setText(message);
//  }
}
