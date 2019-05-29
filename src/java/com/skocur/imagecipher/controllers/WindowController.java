package com.skocur.imagecipher.controllers;

import java.io.File;
import java.io.IOException;

import com.skocur.imagecipher.Decrypter;
import com.skocur.imagecipher.encrypters.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * <h1>WindowController</h1>
 * <br>
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

    // Default option is Low Level Bit Encryption/Decryption
    private int cryptoOption = 3;

    @Override
    public void start(Stage myStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../views/MainWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 1178, 533);
        myStage.setMinWidth(900);
        myStage.setMinHeight(450);
        myStage.setTitle("Image Cipher");
        myStage.setScene(scene);
        myStage.show();
    }

    private void initEncryptionMode() {
        for (int i = 0; i < encryptionMode.getItems().size(); i++) {
            int finalI = i;
            encryptionMode.getItems().get(i).setOnAction(e ->
                    cryptoOption = finalI + 1
            );
        }

        encryptButton.setDisable(false);
    }

    private void initDecryptionMode() {
        for (int i = 0; i < decryptionMode.getItems().size(); i++) {
            int finalI = i;
            decryptionMode.getItems().get(i).setOnAction(e ->
                    cryptoOption = finalI + 1
            );
        }

        decryptButton.setDisable(false);
    }

    @FXML
    public void loadImage() {
        File file = new File(imagePathTextField.getText());
        Image image = new Image(file.toURI().toString());
        previewImage.setImage(image);

        if (file.exists()) {
            initEncryptionMode();
            initDecryptionMode();
        } else {
            encryptButton.setDisable(true);
            decryptButton.setDisable(true);
        }
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
