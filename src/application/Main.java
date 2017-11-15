package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Main extends Application {

	TextField fileName;
	TextField fieldTextToEncrypt;
	VBox vbox;
	HBox hbox;
	Separator separator;
	Button btnDecrypt;
	Button btnEncrypt;
	Label lblTextToEncrypt;
	Label lblResult;

	ObservableList hList;
	ObservableList list;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage myStage) throws Exception {
		myStage.setTitle("Image Cipher (alpha)");

		vbox = new VBox();
		hbox = new HBox();

		lblResult = new Label("Result: ");
		btnDecrypt = new Button("Decrypt");
		btnEncrypt = new Button("Encrypt");
		lblTextToEncrypt = new Label("Text: ");
		separator = new Separator();
		fileName = new TextField();
		fieldTextToEncrypt = new TextField();

		fileName.setPromptText("Image name to process: ");
		fileName.setText("some_image.png");
		fileName.setPrefColumnCount(15);

		fieldTextToEncrypt.setPromptText("Text to encrypt");
		fieldTextToEncrypt.setPrefColumnCount(40);

		hbox.setSpacing(10);
		hbox.setMargin(lblTextToEncrypt, new Insets(1, 1, 1, 20));
		hbox.setMargin(fieldTextToEncrypt, new Insets(1, 1, 1, 1));

		vbox.setSpacing(10);
		vbox.setMargin(btnEncrypt, new Insets(0, 10, 0, 20));
		vbox.setMargin(fileName, new Insets(20, 20, 5, 20));
		vbox.setMargin(btnDecrypt, new Insets(5, 5, 5, 20));
		vbox.setMargin(separator, new Insets(10, 10, 10, 10));
		vbox.setMargin(lblResult, new Insets(10, 10, 10, 10));

		separator.setPrefWidth(180);

		hList = hbox.getChildren();
		hList.addAll(lblTextToEncrypt, fieldTextToEncrypt);

		list = vbox.getChildren();
		list.addAll(fileName, btnDecrypt, separator, hbox, btnEncrypt, lblResult);

		btnDecrypt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				try{
					lblResult.setText(Decrypter.decrypt(fileName.getText()));
				} catch (IOException error) {
					System.out.println("Error: " + error);
				}
			}
		});

		btnEncrypt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e){
				try{
					Encrypter.encrypt(fileName.getText(), fieldTextToEncrypt.getText());
				} catch (IOException error){
					System.out.println("Error: " + error);
				}
			}
		});

		Scene myScene = new Scene(vbox, 600, 300);
		myStage.setScene(myScene);
		myStage.setMinHeight(300);
		myStage.setMinWidth(600);

		myStage.show();
	}
}
