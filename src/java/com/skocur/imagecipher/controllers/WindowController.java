package com.skocur.imagecipher.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.skocur.imagecipher.Decrypter;
import com.skocur.imagecipher.Main;
import com.skocur.imagecipher.encrypters.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * WindowController
 * This class is responsible for creating graphical user interface (GUI).
 *
 * @author Szymon Kocur
 */
public class WindowController extends Application {

    @FXML
    public TextField imagePathTextField;
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

    // Default option is Low Level Bit Encryption/Decryption
    private int cryptoOption = 3;

    public static String fileName = "";

    @Override
    public void start(Stage myStage) {
        Optional<Parent> root = Optional.empty();
        try {
            root = Optional.of(new FXMLLoader().load(
                    Main.class.getResourceAsStream("views/MainWindow.fxml")
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        root.ifPresent(parent -> {
            Scene scene = new Scene(parent, 1178, 533);
            scene.getStylesheets().add(
                    Main.class.getResource("views/styles/style_main_window.css").toString()
            );

            myStage.setMinWidth(900);
            myStage.setMinHeight(450);
            myStage.setTitle("Image Cipher");
            myStage.setScene(scene);
            myStage.show();
        });
    }

    private void initCryptoMode() {
        int size = decryptionMode.getItems().size();

        if (size == encryptionMode.getItems().size()) {
            for (int i = 0; i < size; i++) {
                int finalI = i;
                decryptionMode.getItems().get(i).setOnAction(e ->
                        cryptoOption = finalI + 1
                );

                encryptionMode.getItems().get(i).setOnAction(e ->
                        cryptoOption = finalI + 1
                );
            }

            decryptButton.setDisable(false);
            encryptButton.setDisable(false);
        }
    }

    /**
     * This method takes image from file path. If file exists, buttons
     * responsible for processing data (for example encryption) will be
     * enabled.
     */
    @FXML
    public void loadImage() {
        fileName = imagePathTextField.getText();
        File file = new File(fileName);
        Image image = new Image(file.toURI().toString());
        previewImage.setImage(image);

        if (file.exists()) {
            initCryptoMode();

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
            root = Optional.of(new FXMLLoader().load(
                    Main.class.getResourceAsStream("views/ImageProcessingWindow.fxml")
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

    @FXML
    public void encrypt() throws IOException {
        Encrypter encrypter;

        switch (cryptoOption) {
            case 1:
                encrypter = new SingleColorEncryption(imagePathTextField.getText());
                break;
            case 2:
                encrypter = new MultiColorEncryption(imagePathTextField.getText());
                break;
            case 3:
                encrypter = new LowLevelBitEncryption(imagePathTextField.getText());
                break;
            default:
                encrypter = new LowLevelBitEncryption(imagePathTextField.getText());
                System.err.println("There is no available such encryption option!");
                System.err.println("LowLevelBitEncryption has been chosen by default");
        }

        encrypter.encrypt(textToEncrypt.getText());
    }

    @FXML
    public void decrypt() throws IOException {
        String message = "";

        switch (cryptoOption) {
            case 1:
                message = Decrypter.decrypt(imagePathTextField.getText());
                break;
            case 2:
                message = Decrypter.decryptBlue(imagePathTextField.getText());
                break;
            case 3:
                Decrypter decrypter = new Decrypter(imagePathTextField.getText());
                message = decrypter.decryptLowLevelBits();
                break;
            default:
                System.err.println("No valid decryption mode was chosen!");
                System.exit(2);
        }

        messageFromImage.setText(message);
    }
}
